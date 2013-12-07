package org.insa.java.controller;

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
	private long emissionPosition = 0;
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

	private void enableFileEmission() {
		sendThread = new Thread(SendFileNI.getInstance(this));
		sendThread.start();
	}
	
	private void performSendFile(User user, File file, Message msg) {
		this.initFile(file);
		this.sendFileTransfertDemand(user);	
		this.moveEmissionStateTo(TransferState.PROCESSING);
	}
	
	private void performReceiveFile() throws FileNotFoundException {
		chatGUI.getStatusBar().beginFileTransferReception();		
		fileOutputStream = new FileOutputStream(this.getFilePath(), true);
		bufferedWriter = new BufferedOutputStream(fileOutputStream);		
	}
	
	private void enableFileReception(User user, FileTransfertDemand msg) {
		receivedThread = new Thread(ReceivedFileNI.getInstance(this, msg.getPortClient(),user));
		receivedThread.start();
	}

	public void receivedMessage(User user, Message msg) throws IOException {
		if(msg instanceof FileTransfertCancel) {
			this.moveEmissionStateTo(TransferState.CANCELED);
			this.emissionProtocol(user, null, msg);
		}
		else if(msg instanceof FileTransfertConfirmation) {
			if (SendFileNI.getInstance(this).getFileTransfertState() == TransferState.PROCESSING) {
				chatGUI.getStatusBar().beginFileTransferEmission();
				this.emissionProtocol(user, null, msg);
			}
		}
		else if(msg instanceof FileTransfertDemand) {
			this.receptionFileName = ((FileTransfertDemand) msg).getName();
			this.receptionFileSize = ((FileTransfertDemand) msg).getSize();
			this.beginReceptionProtocol(user, msg);
		}
		else if(msg instanceof FilePart) {
			receptionPosition += ((FilePart) msg).getFilePart().length;
			chatGUI.getStatusBar().setReceptionBarValue((int) (receptionPosition*100/receptionFileSize));
			bufferedWriter.write(((FilePart) msg).getFilePart());
			bufferedWriter.flush();
			if(((FilePart) msg).isLast()) {
				bufferedWriter.close();
				this.moveReceptionStateTo(TransferState.TERMINATED);
				this.receptionProtocol(user,null);
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
			this.emissionProtocol(user, file,null);
		}
		else
			chatGUI.getStatusBar().setMessageBarText("A file emission is already processing ! You must wait the end or cancel it");
	}
	
	public void beginReceptionProtocol(User user, Message msg) {
		if(ReceivedFileNI.getInstance(this,((FileTransfertDemand) msg).getPortClient(),user).getFileTransfertState() == TransferState.AVAILABLE) {
			try {
				this.receptionProtocol(user,(FileTransfertDemand) msg);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			
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
			this.fileEmissionCanceled();
			break;
		}
	}
	
	public void receptionProtocol(User user, FileTransfertDemand msg) throws FileNotFoundException {
		switch(ReceivedFileNI.getInstance(this, msg.getPortClient(),user).getFileTransfertState()) {
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
			this.enableFileReception(user, msg);
			break;
		case TERMINATED :
			this.finishFileReception();
			break;
		case CANCELED :
			this.fileReceptionCanceled();
			break;
		}
	}

	public void moveReceptionStateTo(TransferState state,User user, FileTransfertDemand msg) {
		ReceivedFileNI.getInstance(this, msg.getPortClient(),user).setFileTransfertState(state);
	}
	
	public void moveReceptionStateTo(TransferState state) {
		ReceivedFileNI.getInstance(this,-1,null).setFileTransfertState(state);
	}
	
	public void moveEmissionStateTo(TransferState state) {
		SendFileNI.getInstance(this).setFileTransfertState(state);
		if(state == TransferState.CANCELED)
			this.emissionProtocol(null, null, null);
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
		this.setEmissionBarValue((int) (emissionPosition*100/emissionFileSize));
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

	public void finishFileEmission() {
		emissionPosition = 0;
		sendThread = null;
		chatGUI.getStatusBar().finishFileTransferEmission();
		this.moveEmissionStateTo(TransferState.TERMINATED);
		SendFileNI.getInstance(this).close();
		SendFileNI.resetInstance();
	}

	public void finishFileReception() {
		receptionPosition = 0;
		receivedThread = null;
		chatGUI.getStatusBar().finishFileTransferReception();
		this.moveReceptionStateTo(TransferState.TERMINATED);
		ReceivedFileNI.getInstance(this,-1,null).close();
		ReceivedFileNI.resetInstance();
	}

	public void fileEmissionCanceled() {
		this.finishFileEmission();
		chatGUI.getStatusBar().setEmmissionBarText("/!\\ Transfer emission canceled /!\\");	
	}

	public void fileReceptionCanceled() {
		this.finishFileReception();
		chatGUI.getStatusBar().setReceptionBarText("/!\\ Transfer emission canceled /!\\");	
	}
}
