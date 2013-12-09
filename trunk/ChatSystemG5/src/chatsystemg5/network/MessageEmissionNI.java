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
 * The MessageEmissionNI is the intermediate between the MessageHandlerNI and the remote system for sending
 * @author belliot
 */
public class MessageEmissionNI implements ToRemoteApp{

    private int UDP_port_dest;
    //private InetAddress IP_dest;
    private DatagramSocket UDP_sock;
    private DatagramPacket message;
    //private Message message_to_send;
    
    /**
     * The MessageEmissionNI is instancied with a unique UDP port
     * It creates a datagram socket to permit the sending of datagram packets
     * @param UDP_port : UDP port used for transfers
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
     * This function is used to initiate a connection with one or several remote system
     * It can be send on broadcast or individually
     * The Hello message is tranformed in an array and a datagram packet is created with the array, its  size, the IP address to send and the UDP port to use
     * @param hi : the Hello message
     * @param IP_dest : the IP address to send
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
            this.send_on_network(buffer, IP_dest);

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
     * This function is used to initiate a disconnection with remote systems
     * It is sent on broadcast
     * The Goodbye message is tranformed in an array and a datagram packet is created with the array, its  size, the IP address to send and the UDP port to use
     * @param bye : the Goodbye message
     * @param IP_dest : the IP address to send (broadcast)
     */
    public void transfer_disconnection(Goodbye bye, InetAddress IP_dest) {
        
        try {
            // enable brodcast
            this.UDP_sock.setBroadcast(true);
            // send bye
            byte[] buffer = ((Message) bye).toArray();
            this.send_on_network(buffer, IP_dest);
            
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
     * This function is used to send a message to a remote system
     * The Text message is tranformed in an array and a datagram packet is created with the array, its  size, the IP address to send and the UDP port to use
     * @param txt : the Text message
     * @param IP_dest : the IP address to send
     */
    public void send_text (Text txt, InetAddress IP_dest) {
        try {
            byte[] buffer = ((Message) txt).toArray();
            this.send_on_network(buffer, IP_dest);
        }
        catch (IOException exc) {
            System.out.println("Send text error\n" + exc);
        }
    }
    
    /**
     * The function returns the IP address for broadcasting on the network
     * @return : the broadcast's IP address
     */
    public static InetAddress get_broadcast() {
        InetAddress broadcast = null;
        try {
            broadcast = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageEmissionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return broadcast;
    }

    @Override
    public void send_on_network(byte[] msg, InetAddress IP_dest) {
        try {
            message = new DatagramPacket(msg, msg.length, IP_dest, UDP_port_dest);
            UDP_sock.send(message);
        } catch (IOException ex) {
            Logger.getLogger(MessageEmissionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}