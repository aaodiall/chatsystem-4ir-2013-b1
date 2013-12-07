package org.insa.java.view;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

import org.insa.java.controller.FileController;
import org.insa.java.controller.TransferState;

import chatSystemCommon.FilePart;
import chatSystemCommon.Message;

import com.sun.istack.internal.logging.Logger;

public final class SendFileNI extends JavaChatNI {
	private static SendFileNI instance = null;

	private ServerSocket serverSocket;
	private Socket socket;

	private TransferState fileTransfertState = TransferState.AVAILABLE;
	private FileController fileController;

	private BufferedOutputStream bufferedWriter;

	private ObjectOutputStream writer;
	
	private SendFileNI(FileController fileController) {
		this.fileController = fileController;
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			
		}
	}

	public final static SendFileNI getInstance(FileController fileController) {
		if(SendFileNI.instance == null) {
			synchronized(SendFileNI.class) {
				if(SendFileNI.instance == null)
					SendFileNI.instance = new SendFileNI(fileController);
			}
		}
		return SendFileNI.instance;
	}
	
	public final static void resetInstance() {
		SendFileNI.instance = null;
	}

	@Override
	public void run(){
        try {
			this.socket = this.serverSocket.accept();
	        this.bufferedWriter = new BufferedOutputStream(socket.getOutputStream());
	        this.writer = new ObjectOutputStream(bufferedWriter);

	        Message msg;
	        while(this.fileTransfertState == TransferState.PROCESSING) {
	            this.writer.reset();
	            msg =  fileController.getFilePartToSend();
	            this.writer.writeObject(msg);
	            this.bufferedWriter.flush();
	            if(((FilePart) msg).isLast())
	            	fileController.finishEmissionTransfer();
	        }
		} catch (IOException e) {
			fileController.cancelEmissionTransfer();
		}
	}
	
	public void close() {
		try {
			this.writer.close();
			this.bufferedWriter.close(); 
			this.socket.close();
			this.serverSocket.close();
		} catch (IOException e) {
			Logger.getLogger(SendFileNI.class).log(Level.SEVERE, "", e);
		}
	}

	public TransferState getFileTransfertState() {
		return fileTransfertState;
	}

	public void setFileTransfertState(TransferState fileTransfertState) {
		this.fileTransfertState = fileTransfertState;
	}
	
	public int getPort() {
		return this.serverSocket.getLocalPort();
	}
}
