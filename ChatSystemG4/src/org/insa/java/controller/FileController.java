package org.insa.java.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.insa.java.factory.MessageFactory;
import org.insa.java.model.ChatModel;
import org.insa.java.model.User;
import org.insa.java.view.JavaChatGUI;
import org.insa.java.view.ReceivedFileNI;
import org.insa.java.view.SendFileNI;
import org.insa.java.view.SendMessageNI;

import chatSystemCommon.FilePart;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Message;

import com.sun.istack.internal.logging.Logger;

public class FileController {
	private final int FILE_PART_SIZE = 10000;
	private final int DEFAULT_CLIENT_PORT = 12345;
	
	//private ChatController chatController;
	private ChatModel chatModel;
	private JavaChatGUI chatGUI;
	
	private String fileName;
	private long receptionFileSize;
	private long emissionFileSize;
	
	private Thread sendThread = null;
	private Thread receivedThread = null;
	
	private File file;
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;
	private int emissionPosition = 0;
	
	public FileController(ChatController chatController, JavaChatGUI chatGUI, ChatModel chatModel) {
		//this.chatController = chatController;
		this.chatGUI = chatGUI;
		this.chatModel = chatModel;
	}

	public void sendFileTransfertDemand(User user) {
		emissionFileSize = file.length();
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertDemandMessage(chatModel.getLocalUsername(), file.getName(), emissionFileSize,DEFAULT_CLIENT_PORT), user.getAddress());
	}

	public void sendFileTransfertConfirmation(User user, boolean isAccepetd, int idDemand) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertConfirmationMessage(chatModel.getLocalUsername(), isAccepetd, idDemand), user.getAddress());
	}
	
	private void sendFile(User user) {
		SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).sendFile(user);
		sendThread = new Thread(SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT));
		sendThread.start();
	}
	
	public void receivedMessage(User user, Message msg) throws IOException {
		if(msg instanceof FileTransfertCancel) {
			this.moveToState(TransferState.CANCELED);
			this.fileTransfertProtocol(user, null, msg);
		}
		else if(msg instanceof FileTransfertConfirmation) {
			if (SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState() == TransferState.PROCESSING) {
				chatGUI.getStatusBar().beginFileTransferEmission((int) emissionFileSize);
				this.fileTransfertProtocol(user, null, msg);
			}
				
		}
		else if(msg instanceof FileTransfertDemand) {
			this.fileName = ((FileTransfertDemand) msg).getName();
			this.receptionFileSize = ((FileTransfertDemand) msg).getSize();
			int option = JOptionPane.showConfirmDialog(null, "Vous avez reçu une demande de transfert de fichier de la part de "+msg.getUsername()+"\n Nom du fichier : "+ this.fileName +"\nTaille (en byte) : "+ this.receptionFileSize +"\n\nVoulez-vous accepter le fichier ?", "Demande de transfert de fichier reçue", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.OK_OPTION) {
				chatGUI.getStatusBar().beginFileTransferReception((int) receptionFileSize);		
				receivedThread = new Thread(ReceivedFileNI.getInstance(this,((FileTransfertDemand) msg).getPortClient()));
				receivedThread.start();
				String repositoryPath = chatGUI.getFilePath();
				fileOutputStream = new FileOutputStream(repositoryPath + "\\" + this.fileName, true);
				this.sendFileTransfertConfirmation(user, true, msg.getId());
			}
			else
				this.sendFileTransfertConfirmation(user, false, msg.getId());
		}
		else if(msg instanceof FilePart) {
			Logger.getLogger(ChatController.class).log(Level.INFO, "Message received >> " + msg.toString());
			
			chatGUI.getStatusBar().setReceptionBarValue(((FilePart) msg).getFilePart().length);
			fileOutputStream.write(((FilePart) msg).getFilePart());
			fileOutputStream.flush();
			if(((FilePart) msg).isLast()) {
				fileOutputStream.close();
				chatGUI.getStatusBar().finishFileTransferReception();
			}
		}
	}

	public void beginFileTransfertProtocol(User user, File file) {
		if(SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState() == TransferState.AVAILABLE) {
			this.file = file;
			this.fileTransfertProtocol(user, file,null);
		}
		else
			Logger.getLogger(ChatController.class).log(Level.INFO, "Imposible d'envoyer un fichier, un transfert est déjà en cours");
	}

	public void fileTransfertProtocol(User user, File file, Message msg) {
		Logger.getLogger(FileController.class).log(Level.INFO, SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState().name());
		switch(SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState()) {
		case AVAILABLE:
			this.readFile(file);
			this.sendFileTransfertDemand(user);	
			this.moveToState(TransferState.PROCESSING);
			break;
		case PROCESSING :
			if(((FileTransfertConfirmation) msg).isAccepted())
				this.sendFile(user);	
			else 
				this.moveToState(TransferState.CANCELED);
			break;
		case TERMINATED :
			this.moveToState(TransferState.AVAILABLE);
			break;
		case CANCELED :
			sendThread = null;			//TODO verify if it is necessary
			chatGUI.getStatusBar().finishFileTransferEmission();
			chatGUI.getStatusBar().setEmmissionBarText("/!\\ Transfer emission canceled (remote user unreachable) /!\\");	
			this.moveToState(TransferState.AVAILABLE);
			break;
		}
	}

	public void moveToState(TransferState state) {
		SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).setFileTransfertState(state);
		if(SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState() == TransferState.CANCELED)
			this.fileTransfertProtocol(null, null, null);
	}

	public void readFile(File file) {
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public FilePart getFilePartToSend() {
		if(emissionPosition + FILE_PART_SIZE >= file.length())
			return this.getFilePart((int) (file.length()-emissionPosition));
		else if (emissionPosition + FILE_PART_SIZE < file.length()) 
			return this.getFilePart(FILE_PART_SIZE);
		return null;
	}
	
	private FilePart getFilePart(int bufferSize) {
		byte[] buf = new byte[bufferSize];
		this.setEmissionBarValue(bufferSize);
		emissionPosition += bufferSize;
		try {
			fileInputStream.read(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bufferSize == FILE_PART_SIZE) {
			return MessageFactory.getFileMessage(chatModel.getLocalUser().getUsername(), buf, false);
		}
		else {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return MessageFactory.getFileMessage(chatModel.getLocalUser().getUsername(), buf, true);
		}
	}
	
	public void setEmissionBarValue(int i ) {
		chatGUI.getStatusBar().setEmissionBarValue(i);
	}
	
	public void finishFileTransferEmission() {
		chatGUI.getStatusBar().finishFileTransferEmission();
		this.moveToState(TransferState.TERMINATED);
		this.fileTransfertProtocol(null, null, null);
	}
	
	public void closeSocket() {
		try {
			if(ReceivedFileNI.getInstance(this,-1).getSocket() != null)
				ReceivedFileNI.getInstance(this,-1).getSocket().close();
			if(ReceivedFileNI.getInstance(this,-1).getServerSocket() != null)
				ReceivedFileNI.getInstance(this,-1).getServerSocket().close();
		} catch(IOException e) {
			
		}
	}
}
