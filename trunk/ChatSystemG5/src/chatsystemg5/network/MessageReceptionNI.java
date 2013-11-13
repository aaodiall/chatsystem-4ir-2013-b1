package chatsystemg5.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MessageReceptionNI extends MessageHandlerNI implements FromRemoteApp {
    
    private int UDP_port;
    private InetAddress IP_dest;
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    private byte[] buffer; 
    private String text;
    
   
    public MessageReceptionNI() {
        try {
            this.UDP_port  = 16000;
            // Créer un datagramme socket
            this.UDP_sock = new DatagramSocket(this.UDP_port);
        }
        catch (SocketException exc) {
            System.out.println("Problem about the UDP socket creation");
        }
        
    }
    
    
    
    @Override
    public void run() {
        
        try {
             // always listenning
            while(true){
                buffer = new byte[256];
                message = new DatagramPacket(buffer, buffer.length);
                UDP_sock.receive(message);
                text = new String(buffer) ;
		text = text.substring(0, message.getLength());
		System.out.println("Reception du port " + this.message.getPort() + " de la machine " + this.message.getAddress() + " : " + text);

            }
        }
        catch (IOException exc) {
            System.out.println("Problème de connection");
        }
    }

    
}