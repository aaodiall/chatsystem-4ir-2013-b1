package View;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import chatSystemCommon.Hello;
import chatSystemCommon.Message;

public class SendNI {
	public SendNI() {
		sendBroadcastMessage(new Hello("Maloubobola",false));
	}
	
	public void sendBroadcastMessage(Message message) {
		DatagramSocket c;
		
		try {
			c = new DatagramSocket(54321);
			c.setBroadcast(true);
			
			byte[] sendData = message.toArray();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"),12345);
			c.send(sendPacket);
			System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void sendMessage(Message message, InetAddress address) {
		
	}

	public static void main(String[] args) {
		SendNI client = new SendNI();
	}
}
