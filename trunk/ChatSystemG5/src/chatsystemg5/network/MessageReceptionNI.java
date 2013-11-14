package chatsystemg5.network;
import chatsystemg5.brain.ChatController;
import chatsystemg5.common.*;

import chatsystemg5.common.Message;
import static chatsystemg5.common.Message.fromArray;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

    
    
// observable ?
public class MessageReceptionNI extends MessageHandlerNI implements FromRemoteApp, Observer {
    
    private ChatController chat_control;
    private int UDP_port;
    private DatagramSocket UDP_sock;
    private InetAddress IP_source;
    private DatagramPacket message;
    private byte[] buffer; 
    private String text;
    
    public MessageReceptionNI (ChatController chat_control) {
        try {
            this.chat_control = chat_control;
            // the recepter always listen on the same port 16000
            this.UDP_port = 16000;
            this.UDP_sock = new DatagramSocket(this.UDP_port);
            this.buffer = new byte[256];
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
                this.UDP_sock.receive(message);
                
                // get the IP of the sender
                this.IP_source = message.getAddress();
                
                this.send_to_controller(buffer, IP_source);
                // get and convert to string content of the buffer
                text = new String(buffer);
                
		text = text.substring(0, message.getLength());
                // Ca sera inutile ensuite pour le connect
                System.out.println(text);

            }
        } catch (IOException ex) {
            Logger.getLogger(MessageReceptionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    @Override
    public void send_to_controller (byte[] array, InetAddress IP_addr) {
        try {
            Message msg = Message.fromArray(array);
            if (msg instanceof Hello) {
                chat_control.perform_connection((Hello) msg, IP_addr);
            }
            if (msg instanceof Goodbye) {
                chat_control.perform_disconnection((Goodbye) msg, IP_addr);
            }
            if (msg instanceof Text) {
                chat_control.perform_send((Text) msg, IP_addr);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(MessageReceptionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // for the implementation of Observer pattern
    
    

    
}