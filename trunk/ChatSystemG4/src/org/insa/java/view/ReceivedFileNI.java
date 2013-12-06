package org.insa.java.view;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.insa.java.controller.FileController;
import org.insa.java.model.User;

import chatSystemCommon.Message;

public final class ReceivedFileNI extends JavaChatNI {
	private static ReceivedFileNI instance = null;

	private FileController fileController;

	private Socket socket;

	private int portClient;
	private boolean running = true;
	private User user;

	private BufferedInputStream bufferedReader;

	private ObjectInputStream reader;

	private ReceivedFileNI(FileController fileController, int portClient, User user) {
		this.fileController = fileController;
		this.portClient = portClient;
		this.user = user;
	}

	public final static ReceivedFileNI getInstance(FileController fileController, int portClient, User user) {
		if(ReceivedFileNI.instance == null) {
			synchronized(ReceivedFileNI.class) {
				if(ReceivedFileNI.instance == null)
					ReceivedFileNI.instance = new ReceivedFileNI(fileController,portClient, user);
			}
		}
		return ReceivedFileNI.instance;
	}
	
	@Override
	public void run() { 
		try {
			socket = new Socket(user.getAddress(), portClient);
            this.bufferedReader = new BufferedInputStream(socket.getInputStream());
            this.reader = new ObjectInputStream(this.bufferedReader);
           
			while(running) {
				Message msg = (Message) reader.readObject();
				fileController.receivedMessage(new User(socket.getInetAddress(),msg.getUsername()),msg);
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				this.reader.close();
				this.bufferedReader.close();
				if( socket != null)
					this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	/*
	private byte[] toByteArray(InputStream is) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();

		while(reads != -1){
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}
	 */
	public void closeSocket() throws IOException {
		if(socket != null) {
			socket.close();
			socket = null;
		}
	}
	
	public void stop() {
		running = false;
	}
	
	public void go() {
		running = true;
	}
}
