package chatsystemg5.network;

import chatSystemCommon.*;

//import java.lang.Thread;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.UnknownHostException;      

    
/**
 *
 * @author belliot
 */
public class MessageEmissionNI implements ToRemoteApp{

    private int UDP_port_dest;
    //private InetAddress IP_dest;
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    //private Message message_to_send;
    
    /**
     *
     * @param UDP_port
     */
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
    
    
    /**
     *
     * @param hi
     * @param IP_dest
     */
    public void transfer_connection(Hello hi, InetAddress IP_dest) {
         try {
            
            if (!hi.isAck()) {
                // enable brodcast
                this.UDP_sock.setBroadcast(true);
            }
                    
            // send hello

            byte[] buffer = ((Message) hi).toArray();
            //System.out.println("Envoi à : " + IP_dest + "\nTaille : " + buffer.length);
            this.message = new DatagramPacket(buffer, buffer.length, IP_dest, this.UDP_port_dest);
            this.UDP_sock.send(message);

            if (!hi.isAck()) {
                // disable broadcast
                this.UDP_sock.setBroadcast(false);
            }
            
        }
        catch (IOException exc) {
            System.out.println("Connection error\n" + exc);
        }
    }
    
    /**
     *
     * @param bye
     * @param IP_dest
     */
    public void transfer_disconnection(Goodbye bye, InetAddress IP_dest) {
        
        try {
            // enable brodcast
            this.UDP_sock.setBroadcast(true);
            // send bye
            byte[] buffer = ((Message) bye).toArray();
            this.message = new DatagramPacket(buffer, buffer.length, IP_dest, this.UDP_port_dest);
            this.UDP_sock.send(message);
            // disbale brodcast
            this.UDP_sock.setBroadcast(false);
            
            // close the sender port ??
            UDP_sock.close();
        }
        catch (IOException exc) {
            System.out.println("Disonnection error\n" + exc);
        }
    }
    
    /**
     *
     * @param txt
     * @param IP_dest
     */
    public void send_text (Text txt, InetAddress IP_dest) {
        try {
            byte[] buffer = txt.toArray();
            message = new DatagramPacket(buffer, buffer.length, IP_dest, UDP_port_dest);
            UDP_sock.send(message);
            
        }
        catch (IOException exc) {
            System.out.println("Send text error\n" + exc);
        }
    }
    
    /**
     *
     * @return
     */
    public static InetAddress get_broadcast() {
        InetAddress broadcast = null;
        try {
            broadcast = InetAddress.getByName("10.1.255.255");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageEmissionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return broadcast;
    }

    /**
     *
     * @param msg
     * @param IP_dest
     */
    @Override
    public void send(Message msg, InetAddress IP_dest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}