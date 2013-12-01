package org.insa.java.view;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.insa.java.controller.FileController;
import org.insa.java.controller.TransferState;
import org.insa.java.model.User;

import chatSystemCommon.FilePart;

public final class SendFileNI extends JavaChatNI {
	private static SendFileNI instance = null;

	private Socket socket;
	private User remoteUser;

	private TransferState fileTransfertState = TransferState.AVAILABLE;
	private FileController fileController;

	private OutputStream outputStream = null;
	
	private SendFileNI(FileController fileController, int clientPort) {
		this.fileController = fileController;
		this.TCP_CLIENT_PORT = clientPort;
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
				socket = new Socket(remoteUser.getAddress(), TCP_CLIENT_PORT);
				outputStream = socket.getOutputStream();

				FilePart fp = fileController.getFilePartToSend();

				if(fp != null) {
					outputStream.write(fp.toArray());
					outputStream.flush();
					outputStream.close();
					if(fp.isLast())
						fileController.finishFileTransferEmission();
				}
			} catch (IOException e) {
				fileController.moveToState(TransferState.CANCELED);
			}
			finally {
				try {
					outputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
			}
		}
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
}
