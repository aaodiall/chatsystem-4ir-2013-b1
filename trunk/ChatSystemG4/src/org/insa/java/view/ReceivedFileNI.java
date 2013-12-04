package org.insa.java.view;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.insa.java.controller.FileController;
import org.insa.java.model.User;

import chatSystemCommon.FilePart;
import chatSystemCommon.Message;

public final class ReceivedFileNI extends JavaChatNI {
	private static ReceivedFileNI instance = null;

	private FileController fileController;

	//private ServerSocket serverSocket;
	private Socket socket;
	private InputStream inputStream;

	private int portClient;
	private boolean running = true;
	private User user;

	private BufferedInputStream readerBuffer;

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
			//The client starts by trying to connect himself
			socket = new Socket(user.getAddress(), portClient);
            this.readerBuffer = new BufferedInputStream(socket.getInputStream());
            this.reader = new ObjectInputStream(this.readerBuffer);
           
            Message msg = null;
            /*
            do {
                msg = reader.readObject();
                if(((FilePart) msg).isLast()){
                     this.fileToReceive.setIsLast(true);
                }
                this.chatNI.filePartReceived(this.fileToReceive.getId(), ((FilePart) msg).getFilePart(), ((FilePart) msg).isLast());

            } while (!((FilePart) msg).isLast());
			*/

			
			//inputStream = socket.getInputStream();
			while(running) {
				try {
					msg = (Message) reader.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if(msg != null)
					fileController.receivedMessage(new User(socket.getInetAddress(),msg.getUsername()),msg);
				
				/*
				 * while(running) {
				Message m = Message.fromArray(this.toByteArray(inputStream));
				fileController.receivedMessage(new User(socket.getInetAddress(),m.getUsername()),m);
				* }
				*/
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			/*
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
		
	}
	
	private byte[] toByteArray(InputStream is) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();

		while(reads != -1){
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}

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
