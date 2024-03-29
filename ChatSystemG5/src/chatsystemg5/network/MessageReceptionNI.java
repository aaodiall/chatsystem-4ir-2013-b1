package chatsystemg5.network;
import chatSystemCommon.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The MessageReceptionNI is the intermediate between the MessageHandlerNI and the remote system for receiving
 * The MessageReceptionNI is a thread
 * @author belliot
 */
public class MessageReceptionNI extends Thread implements FromRemoteApp {
    
    private MessageHandlerNI msg_handler;
    private int UDP_port;
    private DatagramSocket UDP_sock;
    private String localhost;
    private InetAddress IP_source;
    private DatagramPacket message;
    private byte[] buffer; 
    private String text;
    
    /**
     * The MessageEmissionNI is instancied with a unique UDP port and the MessageHandlerNI object
     * It instantiates the MessageHandlerNI and the UDP port
     * It creates a datagram socket, listening on the network and waiting for datagram packets
     * @param msg_handler : the unique MessageHandlerNI used to transfer messages
     * @param UDP_port : the UDP port to listen on
     */
    public MessageReceptionNI (MessageHandlerNI msg_handler, int UDP_port) {
        try {
            this.setPriority(MAX_PRIORITY);
            this.msg_handler = msg_handler;
            // the recepter always listen on the same port 16001
            this.UDP_port = UDP_port;
            
            this.UDP_sock = new DatagramSocket(this.UDP_port, InetAddress.getByName("0.0.0.0"));
            this.UDP_sock.setBroadcast(true);
            
            this.localhost = (InetAddress.getLocalHost()).getHostAddress();
        }
        catch (IOException exc) {
            System.out.println("Connection error\n" + exc);
        }
    }
    
    @Override
    public void run() {
        
        try {
            // always listenning
            while(msg_handler.get_user_state()){    
                this.buffer = new byte[1000];
                this.message = new DatagramPacket(buffer, buffer.length);
                
                // listen and receive all the message
                this.UDP_sock.receive(message);

                // get the IP of the sender
                this.IP_source = message.getAddress();
                
                //System.out.println("I'm Network : I'm : " + (InetAddress.getLocalHost()).getHostAddress().toString());
                //System.out.println("I'm Network : From : " + IP_source.getHostAddress().toString());
                
                Message msg = Message.fromArray(buffer);
                
                if (!(IP_source.getHostAddress()).equals(this.localhost)) {
                    // send the content of the buffer to the controller
                    msg_handler.receive(msg, IP_source);  
                }         
                else if (!msg_handler.get_user_state()) {
                    // Permet de fermer la socket UDP de reception si le Goodbye vient d'ici
                    this.close_UDP_sock();
                }
                //System.out.println("NI : Test 1");
            }
        } catch (IOException ex) {
            System.out.println("Connection error");
            Logger.getLogger(MessageReceptionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    /**
     * This function closes the reception socket
     */
    public void close_UDP_sock() {
        this.UDP_sock.close();
    }
    
    @Override
    public void receive_from_network (byte[] msg, InetAddress IP_addr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}