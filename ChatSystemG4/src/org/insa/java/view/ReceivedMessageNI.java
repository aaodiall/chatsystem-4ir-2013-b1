package org.insa.java.view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;

import org.insa.java.controller.ChatController;

import chatSystemCommon.Message;

import com.sun.istack.internal.logging.Logger;

/**
 * Network interface used for receiving UDP messages.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public final class ReceivedMessageNI extends JavaChatNI {
	private static ReceivedMessageNI instance = null;
	
	private ChatController chatController;

	/**
	 * Private constructor
	 * @param chatController
	 */
	private ReceivedMessageNI(ChatController chatController) {
		this.chatController = chatController;
	}
	
	/**
	 * Get an unique instance of the class.
	 * @param chatController Generic controller.
	 * @return instance Unique instance of the class.
	 */
	public final static ReceivedMessageNI getInstance(ChatController chatController) {
		if(ReceivedMessageNI.instance == null) {
			synchronized(ReceivedMessageNI.class) {
				if(ReceivedMessageNI.instance == null)
					ReceivedMessageNI.instance = new ReceivedMessageNI(chatController);
			}
		}
		return ReceivedMessageNI.instance;
	}

	@Override
	public void run() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(UDP_SERVER_PORT);
			socket.setBroadcast(true);
			while (true) {
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);
				chatController.receivedMessage(packet.getAddress(),Message.fromArray(packet.getData()));
			}
		} catch (IOException e) {
			chatController.messageError("Impossible to perform connect. Maybe this application is already running.");
			//Logger.getLogger(ReceivedMessageNI.class).log(Level.SEVERE, "", e);
		}
		finally {
			try {
				socket.close();
			} catch(Exception e) {
				Logger.getLogger(ReceivedMessageNI.class).log(Level.SEVERE, "", e);
			}
		}
	}
}
