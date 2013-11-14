package chatsystemg5.network;
import chatsystemg5.common.*;

import chatsystemg5.common.Message;
import static chatsystemg5.common.Message.fromArray;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

    
    
    
public class MessageReceptionNI extends MessageHandlerNI implements FromRemoteApp {
    
    private int UDP_port;
    private DatagramSocket UDP_sock;
    private InetAddress IP_dest;
    private DatagramPacket message;
    private byte[] buffer; 
    private String text;
   
    public MessageReceptionNI() {     
    }

    @Override
    public void run() {
        
        try {
            
            // the recepter always listen on the same port 16000
            this.UDP_port = 16000;
            this.UDP_sock = new DatagramSocket(this.UDP_port);

             // always listenning
            while(true){
                
                this.buffer = new byte[256];
                this.message = new DatagramPacket(buffer, buffer.length);
                this.UDP_sock.receive(message);
                
                InetAddress IP_dest = message.getAddress();
                text = new String(buffer) ;
		text = text.substring(0, message.getLength());
                // Ca sera inutile ensuite pour le connect
                System.out.println("Reception from port " + this.message.getPort() + " of machine " + this.message.getAddress() + " : " + text);

            }
        }
        catch (IOException exc) {
            System.out.println("Connection error\n" + exc);
        }
    }

    @Override
    public Message receive (byte[] array) {
        try {
            Message msg = Message.fromArray(array);
            if (msg instanceof Hello) {
                return ((Hello) msg);
            }
            if (msg instanceof Goodbye) {
                return ((Goodbye) msg);
            }
            if (msg instanceof Text) {
                return  ((Text) msg);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(MessageReceptionNI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}