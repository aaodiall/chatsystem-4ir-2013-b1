package org.insa.java.view;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;

import org.insa.controller.FileController;
import org.insa.controller.TransferState;
import org.insa.model.User;

import chatSystemCommon.FilePart;

import com.sun.istack.internal.logging.Logger;

public final class SendFileNI implements Runnable{
	private static SendFileNI instance = null;

	private Socket socket;
	private User remoteUser;

	private TransferState fileTransfertState = TransferState.AVAILABLE;
	private FileController fileController;
	private int portClient;

	private OutputStream outputStream = null;
	
	private SendFileNI(FileController fileController, int portClient) {
		this.fileController = fileController;
		this.portClient = portClient;
	}

	public final static SendFileNI getInstance(FileController fileController, int portClient) {
		if(SendFileNI.instance == null) {
			synchronized(SendFileNI.class) {
				if(SendFileNI.instance == null)
					SendFileNI.instance = new SendFileNI(fileController,portClient);
			}
		}
		return SendFileNI.instance;
	}

	synchronized public void sendFile(User user){
		this.remoteUser = user;
	}

	@Override
	public void run(){
		while(this.fileTransfertState == TransferState.PROCESSING) {
			try {
				socket = new Socket(remoteUser.getAddress(), portClient);
				outputStream = socket.getOutputStream();
				
				FilePart fp = fileController.getFilePartToSend();
				if(fp == null) {
					fileController.moveToState(TransferState.TERMINATED);
					fileController.fileTransfertProtocol(null, null, null);
				}	
				else {
					outputStream.write(fp.toArray());
					outputStream.flush();
					outputStream.close();
				}
			} catch (InterruptedIOException e) {
				Thread.currentThread().interrupt();
				try {
					outputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
			} catch (IOException e) {
				Logger.getLogger(SendFileNI.class).log(Level.SEVERE,null,e);
			}
		}
	}

	public TransferState getFileTransfertState() {
		return fileTransfertState;
	}

	public void setFileTransfertState(TransferState fileTransfertState) {
		this.fileTransfertState = fileTransfertState;
	}
}
