package org.insa.java.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
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
	
	private ChatModel chatModel;
	private JavaChatGUI chatGUI;
	
	private String receptionFileName;
	private long receptionFileSize;
	private BufferedOutputStream bufferedWriter;
	private FileOutputStream fileOutputStream;
	private Thread receivedThread = null;
	
	private long emissionFileSize;
	private File emissionFile;
	private FileInputStream fileInputStream;
	private int emissionPosition = 0;
	private Thread sendThread = null;
	
	public FileController(JavaChatGUI chatGUI, ChatModel chatModel) {
		this.chatGUI = chatGUI;
		this.chatModel = chatModel;
	}

	public void sendFileTransfertDemand(User user) {
		emissionFileSize = emissionFile.length();
		int clientPort = SendFileNI.getInstance(this).getPort();
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertDemandMessage(chatModel.getLocalUsername(), emissionFile.getName(), emissionFileSize,clientPort), user.getAddress());
	}

	public void sendFileTransfertConfirmation(User user, boolean isAccepetd, int idDemand) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertConfirmationMessage(chatModel.getLocalUsername(), isAccepetd, idDemand), user.getAddress());
	}
	
	private void sendFile() {
		sendThread = new Thread(SendFileNI.getInstance(this));
		sendThread.start();
	}
	
	public void receivedMessage(User user, Message msg) throws IOException {
		if(msg instanceof FileTransfertCancel) {
			this.moveToState(TransferState.CANCELED);
			this.fileTransfertProtocol(user, null, msg);
		}
		else if(msg instanceof FileTransfertConfirmation) {
			if (SendFileNI.getInstance(this).getFileTransfertState() == TransferState.PROCESSING) {
				chatGUI.getStatusBar().beginFileTransferEmission((int) emissionFileSize);
				this.fileTransfertProtocol(user, null, msg);
			}
		}
		else if(msg instanceof FileTransfertDemand) {
			this.receptionFileName = ((FileTransfertDemand) msg).getName();
			this.receptionFileSize = ((FileTransfertDemand) msg).getSize();
			int option = JOptionPane.showConfirmDialog(null, "You received a file transfer demand from "+msg.getUsername()+"\nFile name : "+ this.receptionFileName +"\nSize (in byte) : "+ this.receptionFileSize +"\n\nDo you want to accept ?", "File transfer demand received", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.OK_OPTION) {
				chatGUI.getStatusBar().beginFileTransferReception((int) receptionFileSize);		
				fileOutputStream = new FileOutputStream(this.getFilePath(), true);
				bufferedWriter = new BufferedOutputStream(fileOutputStream);
				
				ReceivedFileNI.getInstance(this,((FileTransfertDemand) msg).getPortClient(),user).go();
				receivedThread = new Thread(ReceivedFileNI.getInstance(this,((FileTransfertDemand) msg).getPortClient(),user));
				receivedThread.start();
				this.sendFileTransfertConfirmation(user, true, ((FileTransfertDemand) msg).getIdDemand());
			}
			else
				this.sendFileTransfertConfirmation(user, false, msg.getId());
		}
		else if(msg instanceof FilePart) {
			chatGUI.getStatusBar().setReceptionBarValue(((FilePart) msg).getFilePart().length);
			bufferedWriter.write(((FilePart) msg).getFilePart());
			bufferedWriter.flush();
			if(((FilePart) msg).isLast()) {
				finishFileTransferReception();
				bufferedWriter.close();
			}
		}
	}
	
	public String getFilePath() {
		String path = chatGUI.getFilePath();
		if(System.getProperty("os.name").toLowerCase().equals("win"))
			path += "\\";
		else
			path += "/";
		path += this.receptionFileName;
		return path;
	}

	public void beginFileTransfertProtocol(User user, File file) {
		if(SendFileNI.getInstance(this).getFileTransfertState() == TransferState.AVAILABLE) {
			this.emissionFile = file;
			this.fileTransfertProtocol(user, file,null);
		}
		else
			chatGUI.getStatusBar().setMessageBarText("A file emission is already processing ! You must wait the end or cancel it");
	}

	public void fileTransfertProtocol(User user, File file, Message msg) {
		Logger.getLogger(FileController.class).log(Level.INFO, SendFileNI.getInstance(this).getFileTransfertState().name());
		switch(SendFileNI.getInstance(this).getFileTransfertState()) {
		case AVAILABLE:
			this.initFile(file);
			this.sendFileTransfertDemand(user);	
			this.moveToState(TransferState.PROCESSING);
			break;
		case PROCESSING :
			if(((FileTransfertConfirmation) msg).isAccepted()) 
				this.sendFile();
			else 
				this.moveToState(TransferState.CANCELED);
			break;
		case TERMINATED :
			this.moveToState(TransferState.AVAILABLE);
			break;
		case CANCELED :
			this.fileEmissionCanceled();
			break;
		}
	}

	public void moveToState(TransferState state) {
		SendFileNI.getInstance(this).setFileTransfertState(state);
		if(SendFileNI.getInstance(this).getFileTransfertState() == TransferState.CANCELED)
			this.fileTransfertProtocol(null, null, null);
	}

	public void initFile(File file) {
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public FilePart getFilePartToSend() {
		if(emissionPosition + FILE_PART_SIZE >= emissionFile.length())
			return this.getFilePart((int) (emissionFile.length()-emissionPosition));
		else if (emissionPosition + FILE_PART_SIZE < emissionFile.length()) 
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
	
	public void setEmissionBarValue(int i) {
		chatGUI.getStatusBar().setEmissionBarValue(i);
	}
	
	public void finishFileTransferEmission() {
		try {
			SendFileNI.getInstance(this).closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		emissionPosition = 0;
		sendThread = null;
		chatGUI.getStatusBar().finishFileTransferEmission();
		this.moveToState(TransferState.TERMINATED);
		this.fileTransfertProtocol(null, null, null);
	}
	
	public void finishFileTransferReception() {
		ReceivedFileNI.getInstance(this, DEFAULT_CLIENT_PORT,null).stop();
		try {
			ReceivedFileNI.getInstance(this, DEFAULT_CLIENT_PORT,null).closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		receivedThread = null;
		chatGUI.getStatusBar().finishFileTransferReception();
	}

	public void fileEmissionCanceled() {
		SendFileNI.getInstance(this).setFileTransfertState(TransferState.CANCELED);
		try {
			SendFileNI.getInstance(this).closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		emissionPosition = 0;
		sendThread = null;
		chatGUI.getStatusBar().finishFileTransferEmission();
		chatGUI.getStatusBar().setEmmissionBarText("/!\\ Transfer emission canceled /!\\");	
		this.moveToState(TransferState.AVAILABLE);
	}

	public void fileReceptionCanceled() {
		this.finishFileTransferReception();
		chatGUI.getStatusBar().setReceptionBarText("/!\\ Transfer emission canceled /!\\");	
	}
}
