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
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    //private Message message_to_send;
    private byte[] buffer;
    
    public MessageEmissionNI (String username){

        this.username = username;
        this.UDP_port_dest = 16001;
        
        try {
            // create a socket with an address we don't care
            this.UDP_sock = new DatagramSocket();
        }
        catch (SocketException ex) {
            Logger.getLogger(MessageEmissionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void transfer_connection(Hello hi) {
         try {
            
//            if (!hi.isAck()) {
//                // enable brodcast
//                this.UDP_sock.setBroadcast(true);
//            }
            
            // Local host :
            //this.IP_dest = InetAddress.getByName("127.0.0.1");
            
            // send hello
            this.buffer = ((Message) hi).toArray();
            this.message = new DatagramPacket(this.buffer, this.buffer.length, this.IP_dest, this.UDP_port_dest);
            this.UDP_sock.send(message);
            System.out.println("Envoi à : " + IP_dest + "\nTaille : " + this.buffer.length);
            
//            if (!hi.isAck()) {
//                // disable broadcast
//                this.UDP_sock.setBroadcast(false);
//            }
        }
        catch (IOException exc) {
            System.out.println("Connection error\n" + exc);
        }
    }
    
    public void transfer_disconnection(Goodbye bye) {
        
        try {
            // enable brodcast
            this.UDP_sock.setBroadcast(true);
            
            // send bye
            buffer = bye.toArray();
            message = new DatagramPacket(buffer, buffer.length, IP_dest, UDP_port);
            UDP_sock.send(message);
            System.out.println("test");
            // disbale brodcast
            this.UDP_sock.setBroadcast(false);
            
            // close the sender port ??
            UDP_sock.close();
        }
        catch (IOException exc) {
            System.out.println("Connection error\n" + exc);
        }
    }
    
    public void send_text (Text txt) {
        try {
            System.out.println("Here send_text : " + IP_dest);
            buffer = txt.toArray();
            message = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("127.0.0.1"), UDP_port);
            UDP_sock.send(message);
        }
        catch (IOException exc) {
            System.out.println("Connection error!!!!\n" + exc);
        }
    }
    
    public InetAddress get_broadcast() {
        InetAddress broadcast = null;
        try {
            broadcast = InetAddress.getByName("10.1.255.255");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageEmissionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return broadcast;
    }
    
    
    @Override
    public void send(Message msg, InetAddress IP_dest){
        this.IP_dest = IP_dest;
        // Si le message est de type Hello
        if (msg instanceof Hello) {
            transfer_connection((Hello) msg);
        }
        if (msg instanceof Goodbye) {
            transfer_disconnection((Goodbye) msg);
        }
        if (msg instanceof Text) {
            send_text((Text) msg);
            System.out.println("Here sendNI : " + IP_dest);
        }

    }
    
}