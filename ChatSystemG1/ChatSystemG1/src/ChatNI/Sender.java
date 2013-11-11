package ChatNI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


import chatSystemCommon.*;

public class Sender {
	 private int toPort;

	 private DatagramSocket socket;
	   
	   
	    public Sender() {
	        
	        this.toPort = 16000;
	        
	        try {
	            socket=new DatagramSocket();
	        }
	        catch(SocketException e){
	        }
	    }
	
	public void SendMessage(Message m,String UsernameIp){
		try {
            System.out.println("on envoie");
            byte[]data=m.toArray();
            DatagramPacket packet=new DatagramPacket(data,
            data.length, java.net.InetAddress.getByName(UsernameIp), toPort);
            socket.send(packet);
           
        }
        catch(IOException ioe) {
            System.err.println(ioe);
        } 
	}
	
	public void BroadCastMessage(Message m){
		try {
            System.out.println("on envoie");
            byte[]data=m.toArray();
            
            DatagramPacket packet=new DatagramPacket(data,
            data.length, java.net.InetAddress.getByName("255.255.255.255"), toPort);
            socket.send(packet);
           
        }
        catch(IOException ioe) {
            System.err.println(ioe);
        } 
	}
	
	public void MultiCastMessage(Message m,String[] UsernameIpList){
		
	}

	
}




