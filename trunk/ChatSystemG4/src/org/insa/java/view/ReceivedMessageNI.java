package org.insa.java.view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.insa.controller.ChatController;

import chatSystemCommon.Message;

public final class ReceivedMessageNI implements Runnable {
	private static ReceivedMessageNI instance = null;
	
	private ChatController chatController;
	
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
			socket = new DatagramSocket(16001, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (true) {
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
}
