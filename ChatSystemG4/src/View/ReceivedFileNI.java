package View;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Controller.ChatController;
import chatSystemCommon.Message;

public final class ReceivedFileNI implements Runnable {
	private static ReceivedFileNI instance = null;

	private ChatController chatController;

	private ReceivedFileNI(ChatController chatController) {
		this.chatController = chatController;
	}

	public final static ReceivedFileNI getInstance(ChatController chatController) {
		if(ReceivedFileNI.instance == null) {
			synchronized(ReceivedFileNI.class) {
				if(ReceivedFileNI.instance == null)
					ReceivedFileNI.instance = new ReceivedFileNI(chatController);
			}
		}
		return ReceivedFileNI.instance;
	}

	public void run() {
		Message msg = null;
		ServerSocket serverSocket = null;
		while(true){ //dont think its a while(true)
			try {
				serverSocket = new ServerSocket(16000);
				Socket skt = serverSocket.accept();   
				ObjectOutputStream objectOutput = new ObjectOutputStream(skt.getOutputStream());
				objectOutput.writeObject(msg);
				chatController.receivedMessage(skt.getInetAddress(), msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
