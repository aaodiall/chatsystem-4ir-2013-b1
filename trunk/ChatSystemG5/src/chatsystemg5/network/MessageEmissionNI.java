package chatsystemg5.network;

import chatSystemCommon.*;

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
import java.nio.Buffer;

    
public class MessageEmissionNI /*extends MessageHandlerNI*/ implements ToRemoteApp{

    private int UDP_port_dest;
    private InetAddress IP_dest;
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    //private Message message_to_send;
    
    public MessageEmissionNI (int UDP_port) {
        
        this.UDP_port_dest = UDP_port;
        
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
            
            if (!hi.isAck()) {
                // enable brodcast
                this.UDP_sock.setBroadcast(true);
            }
                    
            // send hello

            byte[] buffer = ((Message) hi).toArray();
            this.message = new DatagramPacket(buffer, buffer.length, this.IP_dest, this.UDP_port_dest);
            this.UDP_sock.send(message);
            //System.out.println("Envoi à : " + IP_dest + "\nTaille : " + this.buffer.length);
            
            if (!hi.isAck()) {
                // disable broadcast
                this.UDP_sock.setBroadcast(false);
            }
            
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
            byte[] buffer = ((Message) bye).toArray();
            this.message = new DatagramPacket(buffer, buffer.length, this.IP_dest, this.UDP_port_dest);
            this.UDP_sock.send(message);
            // disbale brodcast
            this.UDP_sock.setBroadcast(false);
            
            // close the sender port ??
            //UDP_sock.close();
        }
        catch (IOException exc) {
            System.out.println("Disonnection error\n" + exc);
        }
    }
    
    public void send_text (Text txt) {
        try {
            byte[] buffer = txt.toArray();
            message = new DatagramPacket(buffer, buffer.length, IP_dest, UDP_port_dest);
            UDP_sock.send(message);
            
        }
        catch (IOException exc) {
            System.out.println("Send text error\n" + exc);
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
    public void send(Message msg, InetAddress IP_dest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}