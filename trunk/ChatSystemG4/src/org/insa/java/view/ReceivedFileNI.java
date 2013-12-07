package org.insa.java.view;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;

import org.insa.java.controller.FileController;
import org.insa.java.controller.TransferState;
import org.insa.java.model.User;

import chatSystemCommon.Message;

import com.sun.istack.internal.logging.Logger;

public final class ReceivedFileNI extends JavaChatNI {
	private static ReceivedFileNI instance = null;

	private FileController fileController;

	private Socket socket;

	private TransferState fileTransfertState = TransferState.AVAILABLE;
	
	private int portClient;
	private User user;

	private BufferedInputStream bufferedReader;

	private ObjectInputStream reader;

	private ReceivedFileNI(FileController fileController, User user) {
		this.fileController = fileController;
		this.user = user;
		this.portClient = fileController.getTransferPort();
	}

	public final static ReceivedFileNI getInstance(FileController fileController, User user) {
		if(ReceivedFileNI.instance == null) {
			synchronized(ReceivedFileNI.class) {
				if(ReceivedFileNI.instance == null)
					ReceivedFileNI.instance = new ReceivedFileNI(fileController, user);
			}
		}
		return ReceivedFileNI.instance;
	}
	
	public final static void resetInstance() {
		ReceivedFileNI.instance = null;
	}
	
	@Override
	public void run() { 
		try {
			socket = new Socket(user.getAddress(), portClient);
            this.bufferedReader = new BufferedInputStream(socket.getInputStream());
            this.reader = new ObjectInputStream(this.bufferedReader);
           
			while(fileTransfertState == TransferState.PROCESSING) {
				Message msg = (Message) reader.readObject();
				fileController.receivedMessage(new User(socket.getInetAddress(),msg.getUsername()),msg);
			}
		}
		catch (ClassNotFoundException e) {
			Logger.getLogger(ReceivedFileNI.class).log(Level.SEVERE, "", e);
		} catch (IOException e) {
			fileController.cancelReceptionTransfer();
		}	
	}

	public void close() {
		try {
			reader.close();
			bufferedReader.close();
			socket.close();
		} catch(IOException e) {
			Logger.getLogger(ReceivedFileNI.class).log(Level.SEVERE, "", e);
		}
	}

	public TransferState getFileTransfertState() {
		return fileTransfertState;
	}

	public void setFileTransfertState(TransferState fileTransfertState) {
		this.fileTransfertState = fileTransfertState;
	}
}
