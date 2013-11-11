package View;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import chatSystemCommon.Message;

public final class SendNI {
	private static SendNI instance = null;
	
	private DatagramSocket datagramSocket;
	
	private SendNI() {
		try {
			datagramSocket = new DatagramSocket(16000);
			datagramSocket.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public final static SendNI getInstance() {
		if(SendNI.instance == null) {
			synchronized(SendNI.class) {
				if(SendNI.instance == null)
					SendNI.instance = new SendNI();
			}
		}
		return SendNI.instance;
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
