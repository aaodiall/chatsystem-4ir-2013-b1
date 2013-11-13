package chatsystemg5.network;

import chatsystemg5.common.*;

//import java.lang.Thread;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;
import java.net.InetAddress;



public class MessageEmissionNI extends MessageHandlerNI implements ToRemoteApp{

    private String username;
    private int UDP_port = 16000;
    private InetAddress IP_dest;
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    private byte[] buffer;
    
    
    public MessageEmissionNI() {
        try {
            // Créer un datagramme socket
            UDP_sock = new DatagramSocket(UDP_port);
        }
        catch (SocketException exc) {
            System.out.println("Problem about the UDP socket creation");
        }
        
    }
    
    
    @Override
    public void run() {
        
        try {
            Hello hi = new Hello(username, false);
            buffer = hi.toArray();
            IP_dest = InetAddress.getLocalHost();
            message = new DatagramPacket(buffer, buffer.length, IP_dest, UDP_port);
            UDP_sock.send(message);
        }
        catch (IOException exc) {
            System.out.println("Problème de connection");
        }
    }
    
    @Override
    public void send () {     
        
    }    
    
}