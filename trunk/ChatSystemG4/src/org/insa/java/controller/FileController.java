package org.insa.java.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

	private ChatModel chatModel;
	private JavaChatGUI chatGUI;

	private String receptionFileName;
	private long receptionFileSize;
	private BufferedOutputStream bufferedWriter;
	private FileOutputStream fileOutputStream;
	private long receptionPosition = 0;
	private Thread receivedThread = null;

	private long emissionFileSize;
	private File emissionFile;
	private FileInputStream fileInputStream;
	private BufferedInputStream bufferedReader;
	private long emissionPosition = 0;
	private Thread sendThread = null;
	
	private int transferPort = -1;

	public FileController(JavaChatGUI chatGUI, ChatModel chatModel) {
		this.chatGUI = chatGUI;
		this.chatModel = chatModel;
	}

	public void sendFileTransfertDemand(User user) {
		emissionFileSize = emissionFile.length();
		int clientPort = SendFileNI.getInstance(this).getPort();
		SendMessageNI.getInstance().sendMessage(MessageFactory.demand(chatModel.getLocalUsername(), emissionFile.getName(), emissionFileSize,clientPort), user.getAddress());
	}

	public void sendFileTransfertConfirmation(User user, boolean isAccepetd, int idDemand) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.confirmation(chatModel.getLocalUsername(), isAccepetd, idDemand), user.getAddress());
	}

	public void receivedMessage(User user, Message msg) throws IOException {
		if(msg instanceof FileTransfertCancel) {
			this.moveEmissionStateTo(TransferState.CANCELED);
			this.emissionProtocol(user, null, msg);
		}
		else if(msg instanceof FileTransfertConfirmation) {
			if (SendFileNI.getInstance(this).getFileTransfertState() == TransferState.PROCESSING) {
				chatGUI.displayEmissionTransferInformation();
				this.emissionProtocol(user, null, msg);
			}
		}
		else if(msg instanceof FileTransfertDemand) {
			this.receptionFileName = ((FileTransfertDemand) msg).getName();
			this.receptionFileSize = ((FileTransfertDemand) msg).getSize();
			this.transferPort = ((FileTransfertDemand) msg).getPortClient();
			this.beginReceptionProtocol(user, msg);
		}
		else if(msg instanceof FilePart) {
			receptionPosition += ((FilePart) msg).getFilePart().length;
			chatGUI.displayReceptionTransferPercent((int) (receptionPosition*100/receptionFileSize));
			bufferedWriter.write(((FilePart) msg).getFilePart());
			bufferedWriter.flush();
			if(((FilePart) msg).isLast()) {
				bufferedWriter.close();
				this.moveReceptionStateTo(TransferState.TERMINATED);
				this.receptionProtocol(user,null);
			}
		}
	}

	private String getFilePath() {
		String path = chatGUI.getFilePath();
		if(System.getProperty("os.name").toLowerCase().equals("win"))
			path += "\\";
		else
			path += "/";
		path += this.receptionFileName;
		return path;
	}

	public void beginEmissionProtocol(User user, File file) {
		if(SendFileNI.getInstance(this).getFileTransfertState() == TransferState.AVAILABLE) {
			this.emissionFile = file;
			this.emissionProtocol(user, file,null);
		}
		else
			chatGUI.getStatusBar().setMessageBarText("A file emission is already processing ! You must wait the end or cancel it");
	}
	
	public void beginReceptionProtocol(User user, Message msg) {
		if(ReceivedFileNI.getInstance(this,user).getFileTransfertState() == TransferState.AVAILABLE) {
			try {
				this.receptionProtocol(user,(FileTransfertDemand) msg);
			} catch (FileNotFoundException e) {
				Logger.getLogger(FileController.class).log(Level.SEVERE, "", e);
			}
		}
	}

	public void emissionProtocol(User user, File file, Message msg) {
		Logger.getLogger(FileController.class).log(Level.INFO, SendFileNI.getInstance(this).getFileTransfertState().name());
		switch(SendFileNI.getInstance(this).getFileTransfertState()) {
		case AVAILABLE:
			this.performSendFile(user,file,msg);
			break;
		case PROCESSING :
			if(((FileTransfertConfirmation) msg).isAccepted()) 
				this.enableFileEmission();		
			else 
				this.moveEmissionStateTo(TransferState.CANCELED);
			break;
		case TERMINATED :
			this.moveEmissionStateTo(TransferState.AVAILABLE);
			break;
		case CANCELED :
			this.cancelEmissionTransfer();
			break;
		}
	}
	
	private void enableFileEmission() {
		sendThread = new Thread(SendFileNI.getInstance(this));
		sendThread.start();
	}
	
	private void performSendFile(User user, File file, Message msg) {
		try {
			fileInputStream = new FileInputStream(file);
			bufferedReader = new BufferedInputStream(fileInputStream);
		} catch (FileNotFoundException e) {
			Logger.getLogger(FileController.class).log(Level.SEVERE, "", e);
		}
		this.sendFileTransfertDemand(user);	
		this.moveEmissionStateTo(TransferState.PROCESSING);
	}
	
	public void receptionProtocol(User user, FileTransfertDemand msg) throws FileNotFoundException {
		switch(ReceivedFileNI.getInstance(this,user).getFileTransfertState()) {
		case AVAILABLE:
			int option = JOptionPane.showConfirmDialog(null, "You received a file transfer demand from "+msg.getUsername()+"\nFile name : "+ this.receptionFileName +"\nSize (in byte) : "+ this.receptionFileSize +"\n\nDo you want to accept ?", "File transfer demand received", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.OK_OPTION) {
				performReceiveFile();
				this.sendFileTransfertConfirmation(user, true, msg.getIdDemand());
				moveReceptionStateTo(TransferState.PROCESSING,user,msg);
				this.receptionProtocol(user, msg);
			}
			else
				this.sendFileTransfertConfirmation(user, false, msg.getIdDemand());
			break;
		case PROCESSING :
			this.enableFileReception(user);
			break;
		case TERMINATED :
			this.finishReceptionTransfer();
			break;
		case CANCELED :
			this.cancelReceptionTransfer();
			break;
		}
	}
	
	private void performReceiveFile() throws FileNotFoundException {
		chatGUI.displayReceptionTransferInformation();
		fileOutputStream = new FileOutputStream(this.getFilePath(), true);
		bufferedWriter = new BufferedOutputStream(fileOutputStream);		
	}
	
	private void enableFileReception(User user) {
		receivedThread = new Thread(ReceivedFileNI.getInstance(this,user));
		receivedThread.start();
	}

	public void moveReceptionStateTo(TransferState state,User user, FileTransfertDemand msg) {
		ReceivedFileNI.getInstance(this,user).setFileTransfertState(state);
	}
	
	public void moveReceptionStateTo(TransferState state) {
		ReceivedFileNI.getInstance(this,null).setFileTransfertState(state);
	}
	
	public void moveEmissionStateTo(TransferState state) {
		SendFileNI.getInstance(this).setFileTransfertState(state);
		if(state == TransferState.CANCELED)
			this.emissionProtocol(null, null, null);
	}

	public FilePart getFilePartToSend() throws IOException{
		if(emissionPosition + FILE_PART_SIZE >= emissionFile.length())
			return this.getFilePart((int) (emissionFile.length()-emissionPosition));
		else if (emissionPosition + FILE_PART_SIZE < emissionFile.length()) 
			return this.getFilePart(FILE_PART_SIZE);
		return null;
	}

	private FilePart getFilePart(int bufferSize) throws IOException{
		byte[] buf = new byte[bufferSize];
		chatGUI.displayEmissionTransferPercent((int) (emissionPosition*100/emissionFileSize));
		emissionPosition += bufferSize;
		bufferedReader.read(buf);
		
		if(bufferSize == FILE_PART_SIZE) {
			return MessageFactory.file(chatModel.getLocalUser().getUsername(), buf, false);
		}
		else {		
			fileInputStream.close();
			bufferedReader.close();
			return MessageFactory.file(chatModel.getLocalUser().getUsername(), buf, true);
		}
	}

	public void finishEmissionTransfer() {
		emissionPosition = 0;
		sendThread = null;
		chatGUI.hideEmissionTransferInformation();
		this.moveEmissionStateTo(TransferState.TERMINATED);
		SendFileNI.getInstance(this).close();
		SendFileNI.resetInstance();
	}

	public void finishReceptionTransfer() {
		receptionPosition = 0;
		receivedThread = null;
		chatGUI.hideReceptionTransferInformation();
		this.moveReceptionStateTo(TransferState.TERMINATED);
		ReceivedFileNI.getInstance(this,null).close();
		ReceivedFileNI.resetInstance();
	}

	public void cancelEmissionTransfer() {
		this.finishEmissionTransfer();
		chatGUI.displayEmissionTransferError("/!\\ Transfer emission canceled /!\\");
	}

	public void cancelReceptionTransfer() {
		this.finishReceptionTransfer();
		chatGUI.displayReceptionTransferError("/!\\ Transfer reception canceled /!\\");
	}
	
	public int getTransferPort() {
		return transferPort;
	}
}
