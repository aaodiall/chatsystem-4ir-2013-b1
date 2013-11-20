package chatsystemg5.network;
import chatsystemg5.brain.ChatController;
import chatSystemCommon.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageReceptionNI /*extends MessageHandlerNI*/ implements Runnable, FromRemoteApp {
    
    private MessageHandlerNI msg_handler;
    private int UDP_port;
    private DatagramSocket UDP_sock;
    private String localhost;
    private InetAddress IP_source;
    private DatagramPacket message;
    private byte[] buffer; 
    private String text;
    
    public MessageReceptionNI (MessageHandlerNI msg_handler, int UDP_port) {
        try {
            this.msg_handler = msg_handler;
            // the recepter always listen on the same port 16001
            this.UDP_port = UDP_port;
            
            this.UDP_sock = new DatagramSocket(this.UDP_port, InetAddress.getByName("0.0.0.0"));
            this.UDP_sock.setBroadcast(true);
            this.buffer = new byte[256];
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
            while(true){     
                
                this.message = new DatagramPacket(buffer, buffer.length);
                
                // listen and receive all the message
                this.UDP_sock.receive(message);
                
                // get the IP of the sender
                this.IP_source = message.getAddress();
                
                //System.out.println(" I'm Network : From : " + IP_source.getHostAddress().toString());
                //System.out.println(" I'm Network : I'm : " + (InetAddress.getLocalHost()).getHostAddress().toString());
                
                if (!(IP_source.getHostAddress()).equals(this.localhost)) {
                    // send the content of the buffer to the controller
                    Message msg = Message.fromArray(buffer);
                    msg_handler.receive(msg, IP_source);  
                }                

            }
        } catch (IOException ex) {
            System.out.println("Connection error");
            Logger.getLogger(MessageReceptionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    @Override
    public void send_to_controller(byte[] array, InetAddress IP_addr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}