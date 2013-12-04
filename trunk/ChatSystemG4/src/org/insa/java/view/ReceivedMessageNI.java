package org.insa.java.view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.insa.java.controller.ChatController;

import chatSystemCommon.Message;

public final class ReceivedMessageNI extends JavaChatNI {
	private static ReceivedMessageNI instance = null;
	
	private ChatController chatController;
	private boolean running = true;

	private ReceivedMessageNI(ChatController chatController) {
		this.chatController = chatController;
	}
	
	public final static ReceivedMessageNI getInstance(ChatController chatController) {
		if(ReceivedMessageNI.instance == null) {
			synchronized(ReceivedMessageNI.class) {
				if(ReceivedMessageNI.instance == null)
					ReceivedMessageNI.instance = new ReceivedMessageNI(chatController);
			}
		}
		return ReceivedMessageNI.instance;
	}

	public void run() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(UDP_SERVER_PORT);
			socket.setBroadcast(true);
			while (running) {
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);
				chatController.receivedMessage(packet.getAddress(),Message.fromArray(packet.getData()));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			socket.close();
		}
	}
	
	public void stop() {
		running = false;
	}
}
