package org.insa.java.view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;

import chatSystemCommon.Message;

import com.sun.istack.internal.logging.Logger;

/**
 * Network interface used to send UDP messages.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public final class SendMessageNI extends JavaChatNI {
	private static SendMessageNI instance = null;
	
	private DatagramSocket datagramSocket;
	
	/**
	 * Private constructor
	 */
	private SendMessageNI() {
		try {
			datagramSocket = new DatagramSocket(UDP_CLIENT_PORT);
			datagramSocket.setBroadcast(true);
		} catch (SocketException e) {
			Logger.getLogger(SendMessageNI.class).log(Level.SEVERE, "", e);
		} catch (Exception e) {
			Logger.getLogger(SendMessageNI.class).log(Level.SEVERE, "", e);
		}
	}
	
	/**
	 * Get an unique instance of the class.
	 * @return instance Unique instance of the class.
	 */
	public final static SendMessageNI getInstance() {
		if(SendMessageNI.instance == null) {
			synchronized(SendMessageNI.class) {
				if(SendMessageNI.instance == null)
					SendMessageNI.instance = new SendMessageNI();
			}
		}
		return SendMessageNI.instance;
	}
	
	/**
	 * Send a broadcast message
	 * @param message Message to send.
	 */
	public void sendBroadcastMessage(Message message) {	
		try {
			this.sendMessage(message, InetAddress.getByName(UDP_BROADCAST_EMISSION));
		} catch (UnknownHostException e) {
			Logger.getLogger(SendMessageNI.class).log(Level.SEVERE, "", e);
		}
	}
	
	/**
	 * Send a message to a specific user.
	 * @param message Message to send.
	 * @param address User address.
	 */
	public void sendMessage(Message message, InetAddress address) {
		try {			
			byte[] sendData = message.toArray();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address,UDP_SERVER_PORT);
			datagramSocket.send(sendPacket);
		} catch (IOException e) {
			Logger.getLogger(SendMessageNI.class).log(Level.SEVERE, "", e);
		} catch (Exception e) {
			Logger.getLogger(SendMessageNI.class).log(Level.SEVERE, "", e);
		}
	}

	@Override
	public void run() {
		// Empty method
	}
}
