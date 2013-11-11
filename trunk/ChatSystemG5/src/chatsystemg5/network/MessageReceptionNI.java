package chatsystemg5.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MessageReceptionNI extends MessageHandlerNI implements FromRemoteApp {
    
    private int UDP_port = 16000;
    private InetAddress IP_dest;
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    private byte[] tampon; 
    
   
    public MessageReceptionNI() {
        try {
            // Créer un datagramme socket
            UDP_sock = new DatagramSocket(UDP_port);
        }
        catch (SocketException exc) {
            System.out.println("Problème de socket UDP");
        }
        
    }
    
    
    
    public void transfer_connection() {
        
        try {
            tampon = new byte[256];
            message = new DatagramPacket(tampon, tampon.length);
            UDP_sock.receive(message);
        }
        catch (IOException exc) {
            System.out.println("Problème de connection");
        }
    }
    
    
}