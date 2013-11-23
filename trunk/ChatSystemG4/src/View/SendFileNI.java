package View;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;

import Controller.FileTransfertController;
import Controller.StateTransfert;
import Model.User;
import chatSystemCommon.FilePart;

import com.sun.istack.internal.logging.Logger;

public final class SendFileNI implements Runnable{
	private static SendFileNI instance = null;

	private Socket socket;
	private User remoteUser;

	private StateTransfert fileTransfertState = StateTransfert.AVAILABLE;
	private FileTransfertController fileTransfertController;

	private OutputStream outputStream = null;
	
	private SendFileNI(FileTransfertController fileTransfertController) {
		this.fileTransfertController = fileTransfertController;
	}

	public final static SendFileNI getInstance(FileTransfertController fileTransfertController) {
		if(SendFileNI.instance == null) {
			synchronized(SendFileNI.class) {
				if(SendFileNI.instance == null)
					SendFileNI.instance = new SendFileNI(fileTransfertController);
			}
		}
		return SendFileNI.instance;
	}

	synchronized public void sendFile(User user){
		Logger.getLogger(SendFileNI.class).log(Level.INFO,"SendFile method called >> "+ user.toString());
		this.remoteUser = user;
		//this.start();
	}

	@Override
	public void run(){
		while(this.fileTransfertState == StateTransfert.PROCESSING) {
			try {
				socket = new Socket(remoteUser.getAddress(), 16001);
				outputStream = socket.getOutputStream();
				
				FilePart fp = fileTransfertController.getFilePartToSend();
				if(fp == null) {
					fileTransfertController.moveToState(StateTransfert.TERMINATED);
					fileTransfertController.fileTransfertProtocol(null, null, null);
				}	
				else {
					outputStream.write(fp.toArray());
					outputStream.flush();
					outputStream.close();
				}
			} 
			catch (IOException e) {
				Logger.getLogger(SendFileNI.class).log(Level.SEVERE,null,e);
			}
		}
	}

	public StateTransfert getFileTransfertState() {
		return fileTransfertState;
	}

	public void setFileTransfertState(StateTransfert fileTransfertState) {
		this.fileTransfertState = fileTransfertState;
	}
}
