package View;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Controller.ChatController;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;

public final class ReceivedNI extends Thread {
	private static ReceivedNI instance = null;
	
	private ChatController chatController;
	
	private ReceivedNI(ChatController chatController) {
		this.chatController = chatController;
		this.run();
	}
	
	public final static ReceivedNI getInstance(ChatController chatController) {
		if(ReceivedNI.instance == null) {
			synchronized(ReceivedNI.class) {
				if(ReceivedNI.instance == null)
					ReceivedNI.instance = new ReceivedNI(chatController);
			}
		}
		return ReceivedNI.instance;
	}

	public void run() {
		try {
			DatagramSocket socket = new DatagramSocket(12345, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (true) {
				System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");
				
				//Receive a packet
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);

				//Packet received
				//System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
				//System.out.println(getClass().getName() + ">>>Packet received; data: " + Message.fromArray(packet.getData()));
				
				chatController.receivedMessage(packet.getAddress(),Message.fromArray(packet.getData()));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}