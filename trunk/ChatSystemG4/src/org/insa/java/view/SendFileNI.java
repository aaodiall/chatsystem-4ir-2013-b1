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

/**
 * Network interface used to send TCP messages.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public final class SendFileNI extends JavaChatNI {
	private static SendFileNI instance = null;

	private ServerSocket serverSocket;
	private Socket socket;

	private TransferState fileTransfertState = TransferState.AVAILABLE;
	private FileController fileController;

	private BufferedOutputStream bufferedWriter;

	private ObjectOutputStream writer;
	
	/**
	 * Private constructor
	 * @param fileController Controller used for file transfer.
	 */
	private SendFileNI(FileController fileController) {
		this.fileController = fileController;
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			
		}
	}

	/**
	 * Get an unique instance of the class.
	 * @param fileController Controller used for file transfer.
	 * @return instance Unique instance of the class.
	 */
	public final static SendFileNI getInstance(FileController fileController) {
		if(SendFileNI.instance == null) {
			synchronized(SendFileNI.class) {
				if(SendFileNI.instance == null)
					SendFileNI.instance = new SendFileNI(fileController);
			}
		}
		return SendFileNI.instance;
	}
	
	/**
	 * Reset instance of the class
	 */
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
	
	/**
	 * Close socket and stream.
	 */
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

	/**
	 * Get emission state.
	 * @return fileTransfertState FileTransferState.
	 */
	public TransferState getFileTransfertState() {
		return fileTransfertState;
	}

	/**
	 * Set emission state.
	 * @param fileTransferState New state value.
	 */
	public void setFileTransfertState(TransferState fileTransfertState) {
		this.fileTransfertState = fileTransfertState;
	}
	
	/**
	 * Get TCP port used for file transfer.
	 * @return port TCP port number
	 */
	public int getPort() {
		return this.serverSocket.getLocalPort();
	}
}
