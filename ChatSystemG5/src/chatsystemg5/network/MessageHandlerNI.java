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

public class MessageHandlerNI {
    
    private int UDP_port;
    private ChatNI chatNI; 
    String username;
    InetAddress IP_dest;
    private MessageEmissionNI msg_emission;
    private Thread msg_reception;
    
    private ChatController chat_control;
    
    public MessageHandlerNI (ChatController chat_control, ChatNI chatNI) {
        
        this.chat_control = chat_control;
        
        UDP_port = 16001;
        this.chatNI = chatNI;
        username = chatNI.get_user();
        
        System.out.println("I'm MsgHandler : view of thread BEF : " + msg_reception);
        // Creation de la rececption
        if (msg_reception == null) {
            System.out.println("Thread créé !");
            msg_reception = new Thread(new MessageReceptionNI(this, UDP_port));
        }
        System.out.println("I'm MsgHandler : view of thread AFT : " + msg_reception);
        msg_reception.start();
        
        // Creation de l'emission
        msg_emission = new MessageEmissionNI(UDP_port);
        
    }
    
    public boolean get_user_state() {
        return chatNI.get_user_state();
    }
    
    /******************************************************************/
    
    // Gère la liaison reception-chatNI
    
    public void receive (Message msg, InetAddress IP_source) {
        String IP_text = IP_source.getHostAddress();
        if (msg instanceof Hello) {
            System.out.println("I'm MsgHandler : Hello received from " + msg.getUsername() + ", IP source : " + IP_text);
            chatNI.from_connection(msg.getUsername(), IP_text, ((Hello) msg).isAck());
            //chat_control.perform_connection(msg.getUsername(), IP_text, ((Hello) msg).isAck());
        }
        if (msg instanceof Goodbye) {
            System.out.println("I'm MsgHandler : Goodbye received.");
            chatNI.from_disconnection(msg.getUsername(), IP_text);
        }
        if (msg instanceof Text) {
            System.out.println("I'm MsgHandler : Text received.");
            chatNI.from_receive_text(msg.getUsername(), ((Text) msg).getText(), IP_text);
        }
    }
    

    /******************************************************************/    
    
    // Gère la liaison chatNI-emission
    
    public void send(Message msg, InetAddress IP_dest){
        this.IP_dest = IP_dest;
        // Si le message est de type Hello
        if (msg instanceof Hello) {
            msg_emission.transfer_connection((Hello) msg, IP_dest);
        }
        if (msg instanceof Goodbye) {
            msg_emission.transfer_disconnection((Goodbye) msg, IP_dest);
        }
        if (msg instanceof Text) {
            msg_emission.send_text((Text) msg, IP_dest);
        }
    }
    
    public void send_connection(InetAddress IP_dest, Boolean alrdythere) {
        //System.out.println("I'm MsgHandler : username : " + username);
        Hello msg = new Hello(username, alrdythere);
//        if (!alrdythere) {
//            IP_dest = msg_emission.get_broadcast();
//        }
        //System.out.println("I'm MsgHandler : sending Hello to " + IP_dest.getHostAddress());
        send(msg, IP_dest);
    }
    
    public void send_disconnection() {
        Goodbye msg = new Goodbye(username);
        IP_dest = msg_emission.get_broadcast();
        send(msg, IP_dest);
        this.msg_reception.interrupt();        
     }
    
    public void send_text(InetAddress IP_dest, String txt) {
        Text msg = new Text(username, txt);
        send(msg, IP_dest);
     }
    
}
