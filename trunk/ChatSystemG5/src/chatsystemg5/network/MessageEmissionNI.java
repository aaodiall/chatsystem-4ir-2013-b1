package chatsystemg5.network;

import chatsystemg5.common.*;

//import java.lang.Thread;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.UnknownHostException;



       
    

    
public class MessageEmissionNI extends MessageHandlerNI implements ToRemoteApp{

    private String username;
    private int UDP_port;
    private int UDP_port_dest;
    private InetAddress IP_dest;
    private InetAddress IP_broadcast;
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    private byte[] buffer;
    
    
    public MessageEmissionNI(String username) {
        try {
            // Donner info sur username
            this.username = username;
            // Créer l'adresse de broadcast
            IP_broadcast = InetAddress.getByName("255.255.255.0");
        }
        catch (UnknownHostException exc) {
            System.out.println("The IP address destination is not known");
        }
        
    }
    
    
    @Override
    public void run() {
        try {
            this.UDP_port_dest = 16000;
            // TO DO : get in the model the IP address associated to the username which has been added in parameters
            this.IP_dest = InetAddress.getLocalHost();
            
            // create a socket with an address we don't care
            this.UDP_sock = new DatagramSocket();

            // create and send the message
            Hello hi = new Hello(username, false);
            this.buffer = hi.toArray();
            this.message = new DatagramPacket(this.buffer, this.buffer.length, this.IP_dest, this.UDP_port_dest);
            this.UDP_sock.send(message);

        }
        catch (IOException exc) {
            System.out.println("Connection error\n" + exc);
        }
    
//    public void transfer_disconnection() {
//        
//        try {
//            Goodbye bye = new Goodbye(username);
//            buffer = bye.toArray();
//            IP_dest = IP_broadcast;
//            message = new DatagramPacket(buffer, buffer.length, IP_dest, UDP_port);
//            UDP_sock.send(message);
//        }
//        catch (IOException exc) {
//            System.out.println("Connection error");
//        }
//    }
    
//    public void send_text (String text) {
//        // Creer un objet Message Text et faire paquet datagramme
//        try {
//            Text msg = new Text(username, text);
//            buffer = msg.toArray();
//            IP_dest = IP_broadcast;
//            message = new DatagramPacket(buffer, buffer.length, IP_dest, UDP_port);
//            UDP_sock.send(message);
//        }
//        catch (IOException exc) {
//            System.out.println("Connection error");
        }

    @Override
    public void send() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }  
    
   
}