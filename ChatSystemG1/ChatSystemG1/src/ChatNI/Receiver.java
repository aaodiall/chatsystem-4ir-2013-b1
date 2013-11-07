package ChatNI;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import chatSystemCommon.Message;

class Receiver extends Thread {
    
    private int myPort;
    
    private DatagramSocket socket;
    
    
    public Receiver(int myPort) {
    
        this.myPort = myPort;
        
        this.socket = socket;
        try {
            socket = new DatagramSocket(myPort);
        }
        catch(SocketException sE) {
        }
    }
    
    //Receives datagram packets
    public void run() {
        
     byte[] buffer = new byte[512];
        while(true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                
                
               Message m = Message.fromArray(packet.getData());
               System.out.println( m.getUsername() + m.getId() + m.getClass());
                
            }	
            catch(IOException iE) {
            }
        } 
    }
    
    
    // Closes the receiving socket
    public void closeReceiveSocket() {
        
        try {
            socket.close();
        }
        catch(Exception e) {
        }
    }
} // End class Receiver
