package ChatNI;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import chatSystemCommon.Message;

class Sender {
    private int toPort;
    private InetAddress toAdr;
    private DatagramSocket socket;
   
   
    public Sender(InetAddress toAdr, int toPort) {
        this.toAdr = toAdr;
        this.toPort = toPort;
        
        try {
            socket=new DatagramSocket();
        }
        catch(SocketException e){
        }
    }
    
    /*Sends datagram packets*/
    public  void sendAway(Message m) {
        
        try {
            
            
            System.out.println("on envoie");
            byte[]data=m.toArray();
            DatagramPacket packet=new DatagramPacket(data,
            data.length, toAdr, toPort);
            socket.send(packet);
           
        }
        catch(IOException ioe) {
            System.err.println(ioe);
        } 
    } 
    
    
    // Closes the socket
    public void closeSendSocket() {
        
        try {
            socket.close();
        }
        catch(Exception e) {
        }
    }
} //End class Sender
