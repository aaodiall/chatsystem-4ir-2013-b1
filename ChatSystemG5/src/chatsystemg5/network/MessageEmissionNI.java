package chatsystemg5.network;

import chatsystemg5.common.*;

//import java.lang.Thread;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;



public class MessageEmissionNI extends MessageHandlerNI implements ToRemoteApp{

    private String username;
    private int UDP_port = 16000;
    private InetAddress IP_dest;
    private InetAddress IP_broadcast;
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    private byte[] buffer;
    
    
    public MessageEmissionNI(String username) {
        try {
            // Donner info sur username
            this.username = username;
            // Créer un datagramme socket
            UDP_sock = new DatagramSocket(UDP_port);
            // Créer l'adresse de broadcast
            IP_broadcast = InetAddress.getByName("255.255.255.0");
        }
        catch (SocketException exc) {
            System.out.println("Problem about the UDP socket creation");
        }
        catch (UnknownHostException exc) {
            System.out.println("The IP address destination is not known");
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
            System.out.println("Connection error");
        }
    }
    
    public void transfer_disconnection() {
        
        try {
            Goodbye bye = new Goodbye(username);
            buffer = bye.toArray();
            IP_dest = IP_broadcast;
            message = new DatagramPacket(buffer, buffer.length, IP_dest, UDP_port);
            UDP_sock.send(message);
        }
        catch (IOException exc) {
            System.out.println("Connection error");
        }
    }
    
    public void send_text (String text) {
        // Creer un objet Message Text et faire paquet datagramme
        try {
            Text msg = new Text(username, text);
            buffer = msg.toArray();
            IP_dest = IP_broadcast;
            message = new DatagramPacket(buffer, buffer.length, IP_dest, UDP_port);
            UDP_sock.send(message);
        }
        catch (IOException exc) {
            System.out.println("Connection error");
        }
    }
    
    
    @Override
    public void send () { 
        
        
    }    
    
   
}