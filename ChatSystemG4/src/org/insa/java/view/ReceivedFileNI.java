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

/**
 * Network interface used for receiving TCP messages.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public final class ReceivedFileNI extends JavaChatNI {
	private static ReceivedFileNI instance = null;

	private FileController fileController;

	private Socket socket;

	private TransferState fileTransferState = TransferState.AVAILABLE;
	
	private int portClient;
	private User localUser;

	private BufferedInputStream bufferedReader;

	private ObjectInputStream reader;

	/**
	 * Private constructor
	 * @param fileController Controller used for file transfer.
	 * @param localUser Local user
	 */
	private ReceivedFileNI(FileController fileController, User localUser) {
		this.fileController = fileController;
		this.localUser = localUser;
		this.portClient = fileController.getTransferPort();
	}

	/**
	 * Get an unique instance of the class.
	 * @param fileController Controller used for file transfer.
	 * @param localUser Local user
	 * @return instance Unique instance of the class.
	 */
	public final static ReceivedFileNI getInstance(FileController fileController, User localUser) {
		if(ReceivedFileNI.instance == null) {
			synchronized(ReceivedFileNI.class) {
				if(ReceivedFileNI.instance == null)
					ReceivedFileNI.instance = new ReceivedFileNI(fileController, localUser);
			}
		}
		return ReceivedFileNI.instance;
	}
	
	/**
	 * Reset instance of the class
	 */
	public final static void resetInstance() {
		ReceivedFileNI.instance = null;
	}
	
	@Override
	public void run() { 
		try {
			socket = new Socket(localUser.getAddress(), portClient);
            this.bufferedReader = new BufferedInputStream(socket.getInputStream());
            this.reader = new ObjectInputStream(this.bufferedReader);
           
			while(fileTransferState == TransferState.PROCESSING) {
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

	/**
	 * Close socket and stream.
	 */
	public void close() {
		try {
			reader.close();
			bufferedReader.close();
			socket.close();
		} catch(IOException e) {
			Logger.getLogger(ReceivedFileNI.class).log(Level.SEVERE, "", e);
		}
	}

	/**
	 * Get reception state.
	 * @return fileTransfertState FileTransferState.
	 */
	public TransferState getFileTransfertState() {
		return fileTransferState;
	}

	/**
	 * Set reception state.
	 * @param fileTransferState New state value.
	 */
	public void setFileTransfertState(TransferState fileTransferState) {
		this.fileTransferState = fileTransferState;
	}
}
