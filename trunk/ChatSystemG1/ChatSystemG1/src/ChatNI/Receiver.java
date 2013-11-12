package ChatNI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import chatSystemCommon.Message;

public class Receiver implements Runnable{

	private DatagramSocket socket;
	private Thread t;
	public Receiver() {
		
		try {
        this.socket = new DatagramSocket(16001);
        }
        catch(SocketException sE) {
        } 
		t = new Thread(this, "Receiver Thread");
	    t.start(); // Start the thread
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 byte[] buffer = new byte[512];
	        while(!socket.isClosed()) {
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




