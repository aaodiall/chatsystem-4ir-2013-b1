package org.insa.java.view;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import chatSystemCommon.Message;

public final class SendMessageNI extends JavaChatNI {
	private static SendMessageNI instance = null;
	
	private DatagramSocket datagramSocket;
	
	private SendMessageNI() {
		try {
			datagramSocket = new DatagramSocket(UDP_CLIENT_PORT);
			datagramSocket.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public final static SendMessageNI getInstance() {
		if(SendMessageNI.instance == null) {
			synchronized(SendMessageNI.class) {
				if(SendMessageNI.instance == null)
					SendMessageNI.instance = new SendMessageNI();
			}
		}
		return SendMessageNI.instance;
	}
	
	public void sendBroadcastMessage(Message message) {	
		try {
			this.sendMessage(message, InetAddress.getByName(UDP_BROADCAST_EMISSION));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message message, InetAddress address) {
		try {			
			byte[] sendData = message.toArray();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address,UDP_SERVER_PORT);
			datagramSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
