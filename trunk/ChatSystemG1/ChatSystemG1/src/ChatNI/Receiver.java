package ChatNI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import chatSystemCommon.Message;

public class Receiver implements Runnable{

	private int myPort;
	    
	private DatagramSocket socket;
	
	public Receiver() {
		// TODO Auto-generated constructor stub
		this.myPort = 16000;
        try {
        this.socket = new DatagramSocket(16000);
        }
        catch(SocketException sE) {
        }
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 byte[] buffer = new byte[512];
	        while(true) {
	            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	            try {
	                socket.receive(packet);
	                this.ReceiveMessage(packet.getData());
	                
	            }	
	            catch(IOException iE) {
	            }
	        } 
	}

		
	public void ReceiveMessage(byte[] MessageToConvert){
		try {
			ChatNetwork.NotifyMessageReceive(Message.fromArray(MessageToConvert));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public void closeReceiveSocket() {
	        
	        try {
	            socket.close();
	        }
	        catch(Exception e) {
	        }
	    }
}




