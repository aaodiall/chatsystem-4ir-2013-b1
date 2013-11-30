package org.insa.java.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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
	private final int FILE_PART_SIZE = 5000;
	private final int DEFAULT_CLIENT_PORT = 12345;
	
	private ChatController chatController;
	private ChatModel chatModel;
	private JavaChatGUI chatGUI;
	
	private ArrayList<byte[]> sendBuffer = new ArrayList<byte[]>();
	private ByteBuffer receivedBuffer;
	private byte[] receivedFile;
	private String fileName;
	private long receptionFileSize;
	private long emissionFileSize;
	
	private Thread sendThread = null;
	private Thread receivedThread = null;
	
	public FileController(ChatController chatController, JavaChatGUI chatGUI, ChatModel chatModel) {
		this.chatController = chatController;
		this.chatGUI = chatGUI;
		this.chatModel = chatModel;
	}

	public void sendFileTransfertDemand(User user,File file) {
		emissionFileSize = file.length();
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertDemandMessage(chatModel.getLocalUsername(), file.getName(), emissionFileSize,DEFAULT_CLIENT_PORT), user.getAddress());
	}

	public void sendFileTransfertConfirmation(User user, boolean isAccepetd, int idDemand) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertConfirmationMessage(chatModel.getLocalUsername(), isAccepetd, idDemand), user.getAddress());
	}

	private void sendFileTransferCanceled(User user, String username, int idDemand) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertCancelMessage(username, idDemand), user.getAddress());
	}
	
	private void sendFile(User user) {
		SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).sendFile(user);
		sendThread = new Thread(SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT));
		sendThread.start();
	}
	
	public void receivedMessage(User user, Message msg) {
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
			this.receivedFile = new byte[(int) this.receptionFileSize];
			this.receivedBuffer = ByteBuffer.wrap(receivedFile);
			int option = JOptionPane.showConfirmDialog(null, "Vous avez reçu une demande de transfert de fichier de la part de "+msg.getUsername()+"\n Nom du fichier : "+ this.fileName +"\nTaille (en byte) : "+ this.receptionFileSize +"\n\nVoulez-vous accepter le fichier ?", "Demande de transfert de fichier reçue", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.OK_OPTION) {
				chatGUI.getStatusBar().beginFileTransferReception((int) receptionFileSize);
				receivedThread = new Thread(ReceivedFileNI.getInstance(chatController,((FileTransfertDemand) msg).getPortClient()));
				receivedThread.start();
				this.sendFileTransfertConfirmation(user, true, msg.getId());
			}
			else
				this.sendFileTransfertConfirmation(user, false, msg.getId());
		}
		else if(msg instanceof FilePart) {
			receivedBuffer.put(((FilePart) msg).getFilePart());
			chatGUI.getStatusBar().setReceptionBarValue(receivedBuffer.position());
			if(((FilePart) msg).isLast()) {		
				chatGUI.getStatusBar().finishFileTransferReception();
				try {
					String directoryPath = chatGUI.getFilePath();
					if(directoryPath != null && !directoryPath.isEmpty()) {
						FileOutputStream fos = new FileOutputStream(directoryPath+"\\"+this.fileName);
					fos.write(this.receivedFile);
					fos.flush();
					fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
	}

	public void beginFileTransfertProtocol(User user, File file) {
		if(SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState() == TransferState.AVAILABLE) {
			this.fileTransfertProtocol(user, file,null);
		}
		else
			Logger.getLogger(ChatController.class).log(Level.INFO, "Imposible d'envoyer un fichier, un transfert est déjà en cours");
	}

	public void fileTransfertProtocol(User user, File file, Message msg) {
		Logger.getLogger(FileController.class).log(Level.INFO, SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState().name());
		switch(SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState()) {
		case AVAILABLE:
			this.splitFile(file);
			this.sendFileTransfertDemand(user,file);	
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
			sendBuffer.clear();
			this.moveToState(TransferState.AVAILABLE);
			break;
		}
	}

	public void moveToState(TransferState state) {
		SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).setFileTransfertState(state);
		if(SendFileNI.getInstance(this,DEFAULT_CLIENT_PORT).getFileTransfertState() == TransferState.CANCELED)
			this.fileTransfertProtocol(null, null, null);
	}
	
	public void splitFile(File file) {
		byte[] bFile = new byte[(int) file.length()];
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Divide the file in parts
		int fileParts = (bFile.length / FILE_PART_SIZE) + 1;
		for(int i=0; i<fileParts; i++){
			if(i+1==fileParts)
				sendBuffer.add(Arrays.copyOfRange(bFile,i*FILE_PART_SIZE,bFile.length));
			else
				sendBuffer.add(Arrays.copyOfRange(bFile,i*FILE_PART_SIZE,i*FILE_PART_SIZE+FILE_PART_SIZE));
		}
	}

	public FilePart getFilePartToSend() {
		byte[] toSend = null;
		if(!sendBuffer.isEmpty()) {
			toSend = sendBuffer.get(0);		
			if(sendBuffer.size() > 1) {
				sendBuffer.remove(0);
				return MessageFactory.getFileMessage(chatModel.getLocalUser().getUsername(), toSend, false);
			}		
			else {
				sendBuffer.remove(0);
				return MessageFactory.getFileMessage(chatModel.getLocalUser().getUsername(), toSend, true);
			}	
		}
		return null;
	}
	
	public void setEmissionBarValue(int i ) {
		chatGUI.getStatusBar().setEmissionBarValue(i);
	}
	
	public void finishFileTransferEmission() {
		chatGUI.getStatusBar().finishFileTransferEmission();
	}
}
