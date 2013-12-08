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

/**
 * Controller used for file transfer.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
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

	/**
	 * Constructor
	 * @param chatGUI Main graphic interface.
	 * @param chatModel Data model. 
	 */
	public FileController(JavaChatGUI chatGUI, ChatModel chatModel) {
		this.chatGUI = chatGUI;
		this.chatModel = chatModel;
	}

	/**
	 * Send a file transfer demand to an user.
	 * @param user Destination user.
	 */
	public void sendFileTransfertDemand(User user) {
		emissionFileSize = emissionFile.length();
		int clientPort = SendFileNI.getInstance(this).getPort();
		SendMessageNI.getInstance().sendMessage(MessageFactory.demand(chatModel.getLocalUsername(), emissionFile.getName(), emissionFileSize,clientPort), user.getAddress());
	}

	/**
	 * Send a file transfer confirmation to an user. This message is send after receiving a file transfer demand.
	 * @param user Destination user.
	 * @param isAccepetd File transfer accepted or not.
	 * @param idDemand Id corresponding to the file transfer demand. 
	 */
	public void sendFileTransfertConfirmation(User user, boolean isAccepetd, int idDemand) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.confirmation(chatModel.getLocalUsername(), isAccepetd, idDemand), user.getAddress());
	}

	/**
	 * Receive method concerning file messages.
	 * @param user User who sent the message.
	 * @param msg Received message.
	 * @throws IOException If there is a problem when trying to write the file in the hard drive.
	 */
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

	/**
	 * Construct a file path depending on the OS.
	 * @return path Correct path constructed for the local OS.
	 */
	private String getFilePath() {
		String path = chatGUI.getFilePath();
		if(System.getProperty("os.name").toLowerCase().equals("win"))
			path += "\\";
		else
			path += "/";
		path += this.receptionFileName;
		return path;
	}

	/**
	 * Begin protocol in order to send a file to a destination user.
	 * @param user Destination user.
	 * @param file File to send.
	 */
	public void beginEmissionProtocol(User user, File file) {
		if(SendFileNI.getInstance(this).getFileTransfertState() == TransferState.AVAILABLE) {
			this.emissionFile = file;
			this.emissionProtocol(user, file,null);
		}
		else
			chatGUI.getStatusBar().setMessageBarText("A file emission is already processing ! You must wait the end or cancel it");
	}
	
	/**
	 * Begin protocol in order to receive a file from a destination user.
	 * @param user Destination user.
	 * @param msg File to receive.
	 */
	public void beginReceptionProtocol(User user, Message msg) {
		if(ReceivedFileNI.getInstance(this,user).getFileTransfertState() == TransferState.AVAILABLE) {
			try {
				this.receptionProtocol(user,(FileTransfertDemand) msg);
			} catch (FileNotFoundException e) {
				Logger.getLogger(FileController.class).log(Level.SEVERE, "", e);
			}
		}
	}

	/**
	 * Emission protocol. Contains state machine and all operations for sending a file to a destination user.
	 * @param user Destination user. Can be null when moving to Canceled state.
	 * @param file File to send. Can be null when moving to Canceled state.
	 * @param msg Generic message that can move the state machine to another state. Can be null when moving to Canceled state.
	 */
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
	
	/**
	 * Enable file emission by creating a new thread. 
	 */
	private void enableFileEmission() {
		sendThread = new Thread(SendFileNI.getInstance(this));
		sendThread.start();
	}
	
	/**
	 * Initialize accesses to hard drive and move to state Processing
	 * @param user Destination user. Can be null when moving to Canceled state.
	 * @param file File to send. Can be null when moving to Canceled state.
	 * @param msg Generic message that can move the state machine to another state. Can be null when moving to Canceled state.
	 */
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
	
	/**
	 * Reception protocol. Contains state machine and all operations for receiving a file from a destination user.
	 * @param user Destination user. Can be null when moving to Canceled state.
	 * @param msg Generic message that can move the state machine to another state. Can be null when moving to Canceled state.
	 * @throws FileNotFoundException raised if there is a problem when saving file on hard drive.
	 */
	public void receptionProtocol(User user, FileTransfertDemand msg) throws FileNotFoundException {
		switch(ReceivedFileNI.getInstance(this,user).getFileTransfertState()) {
		case AVAILABLE:
			int option = JOptionPane.showConfirmDialog(null, "You received a file transfer demand from "+msg.getUsername()+"\nFile name : "+ this.receptionFileName +"\nSize (in byte) : "+ this.receptionFileSize +"\n\nDo you want to accept ?", "File transfer demand received", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.OK_OPTION) {
				performReceiveFile();
				this.sendFileTransfertConfirmation(user, true, msg.getIdDemand());
				moveReceptionStateTo(TransferState.PROCESSING,user);
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
	
	/**
	 * Enable file reception from a destination by creating a new thread.
	 * @param user Destination user.
	 */
	private void enableFileReception(User user) {
		receivedThread = new Thread(ReceivedFileNI.getInstance(this,user));
		receivedThread.start();
	}
	
	/**
	 * Initialize accesses to hard drive.
	 * @throws FileNotFoundException Raised if the file is not found on the hard drive.
	 */
	private void performReceiveFile() throws FileNotFoundException {
		chatGUI.displayReceptionTransferInformation();
		fileOutputStream = new FileOutputStream(this.getFilePath(), true);
		bufferedWriter = new BufferedOutputStream(fileOutputStream);		
	}

	/**
	 * Update reception state machine to another state when receiving a message from a destination user
	 * @param state New state. 
	 * @param user Destination user.
	 */
	public void moveReceptionStateTo(TransferState state,User user) {
		ReceivedFileNI.getInstance(this,user).setFileTransfertState(state);
	}
	
	/**
	 * Update reception state machine to another state.
	 * @param state New state. 
	 */
	public void moveReceptionStateTo(TransferState state) {
		ReceivedFileNI.getInstance(this,null).setFileTransfertState(state);
	}
	
	/**
	 * Update emission state machine to another state.
	 * @param state New state. 
	 */
	public void moveEmissionStateTo(TransferState state) {
		SendFileNI.getInstance(this).setFileTransfertState(state);
		if(state == TransferState.CANCELED)
			this.emissionProtocol(null, null, null);
	}

	/**
	 * Get next part of the file to send.
	 * @return FilePart A FilePart message composed of a piece of the file.
	 * @throws IOException Raised if there is some problem to access to the file.
	 */
	public FilePart getFilePartToSend() throws IOException{
		if(emissionPosition + FILE_PART_SIZE >= emissionFile.length())
			return this.getFilePart((int) (emissionFile.length()-emissionPosition));
		else if (emissionPosition + FILE_PART_SIZE < emissionFile.length()) 
			return this.getFilePart(FILE_PART_SIZE);
		return null;
	}

	/**
	 * Get a piece of file depending of a buffer size and the last piece of file sent.
	 * @param bufferSize Buffer size
	 * @return filePart A FilePart message composed of a piece of the file.
	 * @throws IOException Raised if there is some problem to access to the file.
	 */
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

	/**
	 * Finish a file emission transfer by stopping emission thread and updating main view.
	 */
	public void finishEmissionTransfer() {
		emissionPosition = 0;
		sendThread = null;
		chatGUI.hideEmissionTransferInformation();
		this.moveEmissionStateTo(TransferState.TERMINATED);
		SendFileNI.getInstance(this).close();
		SendFileNI.resetInstance();
	}

	/**
	 * Finish a file emission transfer by stopping reception thread and updating main view.
	 */
	public void finishReceptionTransfer() {
		receptionPosition = 0;
		receivedThread = null;
		chatGUI.hideReceptionTransferInformation();
		this.moveReceptionStateTo(TransferState.TERMINATED);
		ReceivedFileNI.getInstance(this,null).close();
		ReceivedFileNI.resetInstance();
	}

	/**
	 * Cancel a file emission transfer by stopping emission thread and updating main view.
	 */
	public void cancelEmissionTransfer() {
		this.finishEmissionTransfer();
		chatGUI.displayEmissionTransferError("/!\\ Transfer emission canceled /!\\");
	}

	/**
	 * Cancel a file emission transfer by stopping reception thread and updating main view.
	 */
	public void cancelReceptionTransfer() {
		this.finishReceptionTransfer();
		chatGUI.displayReceptionTransferError("/!\\ Transfer reception canceled /!\\");
	}
	
	/**
	 * Get the port opened for the file transfer.
	 * @return port Port number.
	 */
	public int getTransferPort() {
		return transferPort;
	}
}
