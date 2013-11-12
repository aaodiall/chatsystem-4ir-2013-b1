package ChatNI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


import chatSystemCommon.*;

public class Sender {
	

	 private DatagramSocket socket;
	   
	   
	    public Sender() {
	        
	        
	        
	        try {
	            socket=new DatagramSocket();
	        }
	        catch(SocketException e){
	        }
	    }
	
	public void SendMessage(Message m,String UsernameIp){
		try {
            
            byte[]data=m.toArray();
            DatagramPacket packet=new DatagramPacket(data,
            data.length, InetAddress.getByName(UsernameIp), 16001);
            socket.send(packet);
           
        }
        catch(IOException ioe) {
            System.err.println(ioe);
        } 
	}
	   
	public void BroadCastMessage(Message m){
		try {
         
            byte[]data=m.toArray();
            
            DatagramPacket packet=new DatagramPacket(data,
            data.length, /*ChatNetwork.BroadcastAddress()*/InetAddress.getByName("255.255.255.255"), 16001);
           
            socket.send(packet);
           
        }
        catch(IOException ioe) {
            System.err.println(ioe);
        } 
	}
	
	public void MultiCastMessage(Message m,String[] UsernameIpList){
		
	}

	public void closeSendSocket() {
		// TODO Auto-generated method stub
		  try {
	            socket.close();
	        }
	        catch(Exception e) {
	        }
	}

	
}




