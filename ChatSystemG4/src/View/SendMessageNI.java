package View;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import chatSystemCommon.Message;

public final class SendMessageNI {
	private static SendMessageNI instance = null;
	
	private DatagramSocket datagramSocket;
	
	private SendMessageNI() {
		try {
			datagramSocket = new DatagramSocket(16000);
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
			this.sendMessage(message, InetAddress.getByName("255.255.255.255"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message message, InetAddress address) {
		try {			
			byte[] sendData = message.toArray();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address,16001);
			datagramSocket.send(sendPacket);
			//System.out.println(getClass().getName() + ">>> Request packet sent to: "+address.toString());		
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
