package org.insa.java.view;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.insa.java.controller.FileController;
import org.insa.java.controller.TransferState;
import org.insa.java.model.User;

import chatSystemCommon.FilePart;
import chatSystemCommon.Message;

public final class SendFileNI extends JavaChatNI {
	private static SendFileNI instance = null;

	private ServerSocket serverSocket;
	private Socket socket;

	private TransferState fileTransfertState = TransferState.AVAILABLE;
	private FileController fileController;

	private OutputStream outputStream = null;

	private BufferedOutputStream writerBuffer;

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

	synchronized public void sendFile() throws IOException{
		socket = serverSocket.accept();
		outputStream = socket.getOutputStream();
	}

	@Override
	public void run(){
		 //waiting for a connection
        try {
			this.socket = this.serverSocket.accept();
			 //we are connected, preparing for a transfert
	        this.writerBuffer = new BufferedOutputStream(socket.getOutputStream());
	        this.writer = new ObjectOutputStream(writerBuffer);

	        Message msg;
	        while(this.fileTransfertState == TransferState.PROCESSING) {
	            //allows sending big files (if we don't do that we keep the file in mem)
	            this.writer.reset();
	            //recover the file's part to send and write it in the socket
	            msg =  fileController.getFilePartToSend();

	            this.writer.writeObject(msg);
	            this.writerBuffer.flush();
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
		
		/*
		while(this.fileTransfertState == TransferState.PROCESSING) {
			try {
				FilePart fp = fileController.getFilePartToSend();

				if(fp != null) {
					outputStream.write(fp.toArray());
					outputStream.flush();
					outputStream.close();
					if(fp.isLast())
						fileController.finishFileTransferEmission();
				}
			} catch (IOException e) {
				fileController.fileEmissionCanceled();
			}
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public void closeSocket() throws IOException {
		if(socket != null) {
			socket.close();
			socket = null;
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
