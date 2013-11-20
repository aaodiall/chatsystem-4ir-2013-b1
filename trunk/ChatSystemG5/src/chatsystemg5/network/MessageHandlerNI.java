package chatsystemg5.network;

import chatSystemCommon.*;
import chatsystemg5.brain.ChatController;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageHandlerNI /*extends ChatNI*/ {
    
    private int UDP_port;
    private ChatController chat_control;
    String username;
    InetAddress IP_dest;
    private MessageEmissionNI msg_emission;
    private Thread msg_reception;
    
    public MessageHandlerNI (ChatController chat_control) {
        
        UDP_port = 16001;
        this.chat_control = chat_control;
        username = chat_control.get_userDB().get_username();
  
        // Creation de la rececption
        msg_reception = new Thread(new MessageReceptionNI(this, UDP_port));
        msg_reception.start();
        
        // Creation de l'emission
        msg_emission = new MessageEmissionNI(UDP_port);
        
    }
    
    /******************************************************************/
    
    // Gère la liaison reception-controller
    
    public void receive (Message msg, InetAddress IP_source) {
        String IP_text = IP_source.getHostAddress();
        if (msg instanceof Hello) {
            System.out.println("I'm MsgHandler : Hello received.");
            chat_control.perform_connection(msg.getUsername(), IP_text, ((Hello) msg).isAck());
        }
        if (msg instanceof Goodbye) {
            System.out.println("I'm MsgHandler : Goodbye received.");
            chat_control.perform_disconnection(msg.getUsername(), IP_text);
        }
        if (msg instanceof Text) {
            System.out.println("I'm MsgHandler : Text received.");
            chat_control.perform_send(msg.getUsername(), ((Text) msg).getText(), IP_text);
        }
    }
    

    /******************************************************************/    
    
    // Gère la liaison controller-emission
    public void send(Message msg, InetAddress IP_dest){
        this.IP_dest = IP_dest;
        // Si le message est de type Hello
        if (msg instanceof Hello) {
            msg_emission.transfer_connection((Hello) msg);
        }
        if (msg instanceof Goodbye) {
            msg_emission.transfer_disconnection((Goodbye) msg);
        }
        if (msg instanceof Text) {
            msg_emission.send_text((Text) msg);
        }
    }
    
    public void send_connection(Boolean alrdythere) {
        Hello msg = new Hello(username, alrdythere);
        IP_dest = msg_emission.get_broadcast();
        send(msg, IP_dest);
    }
    
    public void send_disconnection() {
        Goodbye msg = new Goodbye(username);
        IP_dest = msg_emission.get_broadcast();
        send(msg, IP_dest);
     }
    
    public void send_text(String username_and_IP, String txt) {
        try {
            Text msg = new Text(username, txt);
            // On récupère l'@IP associée à l'username
            String IP_text = chat_control.get_listDB().get_IP_addr(username_and_IP);
            // On transforme l'@IP de String à InetAddress
            IP_dest = InetAddress.getByName(IP_text);
            send(msg, IP_dest);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageHandlerNI.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
}
