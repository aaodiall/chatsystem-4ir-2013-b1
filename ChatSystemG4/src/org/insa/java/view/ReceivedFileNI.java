package org.insa.java.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.insa.java.controller.ChatController;

import chatSystemCommon.Message;

public final class ReceivedFileNI extends JavaChatNI {
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

	public void closeSocket() {
		try {
			socket.close();
			serverSocket.close();
		}catch(IOException e) {
			
		}
		
	}
	
	@Override
	public void run() { 
		while(true) {
			try {
				socket = serverSocket.accept();	
				inputStream = socket.getInputStream();
				chatController.receivedMessage(socket.getInetAddress(), Message.fromArray(this.toByteArray(inputStream)));
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
}
