package org.insa.java.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.insa.controller.ChatController;

import chatSystemCommon.Message;

public final class ReceivedFileNI implements Runnable {
	private static ReceivedFileNI instance = null;

	private ChatController chatController;

	private ServerSocket serverSocket;
	private Socket socket;
	private InputStream inputStream;

	public boolean running = true;

	private ReceivedFileNI(ChatController chatController, int portClient) {
		this.chatController = chatController;
		try {
			serverSocket = new ServerSocket(portClient);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final static ReceivedFileNI getInstance(ChatController chatController, int portClient) {
		if(ReceivedFileNI.instance == null) {
			synchronized(ReceivedFileNI.class) {
				if(ReceivedFileNI.instance == null)
					ReceivedFileNI.instance = new ReceivedFileNI(chatController,portClient);
			}
		}
		return ReceivedFileNI.instance;
	}

	@Override
	public void run() { 
		while(true) {
			try {
				socket = serverSocket.accept();	
				inputStream = socket.getInputStream();
				chatController.receivedMessage(socket.getInetAddress(), Message.fromArray(this.toByteArray(inputStream)));
				inputStream.close();
			} catch (InterruptedIOException e) {
				Thread.currentThread().interrupt();
				try {
					inputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
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
}