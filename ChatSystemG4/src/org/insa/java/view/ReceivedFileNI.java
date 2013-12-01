package org.insa.java.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.insa.java.controller.FileController;
import org.insa.java.model.User;

import chatSystemCommon.Message;

public final class ReceivedFileNI extends JavaChatNI {
	private static ReceivedFileNI instance = null;

	private FileController fileController;

	private ServerSocket serverSocket;
	private Socket socket;
	private InputStream inputStream;

	public boolean running = true;

	private ReceivedFileNI(FileController fileController, int portClient) {
		this.fileController = fileController;
		try {
			if(portClient > 1024)
				serverSocket = new ServerSocket(portClient);
		} catch (IOException e) {
			
		}
	}

	public final static ReceivedFileNI getInstance(FileController fileController, int portClient) {
		if(ReceivedFileNI.instance == null) {
			synchronized(ReceivedFileNI.class) {
				if(ReceivedFileNI.instance == null)
					ReceivedFileNI.instance = new ReceivedFileNI(fileController,portClient);
			}
		}
		return ReceivedFileNI.instance;
	}
	
	@Override
	public void run() { 
		while(running) {
			try {
				socket = serverSocket.accept();	
				inputStream = socket.getInputStream();
				Message m = Message.fromArray(this.toByteArray(inputStream));
				fileController.receivedMessage(new User(socket.getInetAddress(),m.getUsername()),m);
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				Thread.currentThread().interrupt( );
				try {
					inputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
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

	public ServerSocket getServerSocket() {
		return serverSocket;
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
}
