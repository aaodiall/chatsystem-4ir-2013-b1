package chatsystemg5.network;

import chatSystemCommon.*;
import chatsystemg5.brain.ChatController;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public /*abstract*/ class ChatNI implements Observer {
    
    private ChatController chat_control;
    private MessageHandlerNI msg_handler;
    //private FileHandlerNI file_handler;
    private String username;
    private InetAddress IP_dest;
    private InetAddress IP_temp;

    public ChatNI(ChatController chat_control) {
        
        this.chat_control = chat_control;
        username = chat_control.get_userDB().get_username();
        msg_handler = new MessageHandlerNI(chat_control, this);
        //file_handler = new FileHandlerNI(this);
        //System.out.println("I'm ChatNI : username : " + username);
    }

    /******************************************************************/
    
    // PARTIE MESSAGE EMISSION
    
    public void to_connection(String user_and_IP, Boolean alrdythere) {
        try {
            if (!alrdythere) {
                IP_dest = MessageEmissionNI.get_broadcast();
            }
            else {
                IP_dest = InetAddress.getByName(chat_control.get_listDB().get_IP_addr(user_and_IP));
            }
            System.out.println("I'm ChatNI : IP dest : " + IP_dest.getHostAddress());
            System.out.println("I'm ChatNI : sending a Hello : " + alrdythere);
            msg_handler.send_connection(IP_dest, alrdythere);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatNI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void to_disconnection() {
        msg_handler.send_disconnection();
    }
    
    public void to_send_text(String username_and_IP, String text) {
        try {
            // On récupère l'@IP associée à l'username
            String IP_text = chat_control.get_listDB().get_IP_addr(username_and_IP);
            // On transforme l'@IP de String à InetAddress
            InetAddress IP_dest = InetAddress.getByName(IP_text);
            msg_handler.send_text(IP_dest, text);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatNI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /******************************************************************/
    
    // PARTIE MESSAGE RECEPTION
    
    public void from_connection(String remote_user, String IP_text, Boolean alrdythere) {
        //try {
            System.out.println("I'm ChatNI : IP source received : " + IP_text);
            //System.out.println("I'm ChatNI : connection back : remote user : " + remote_user + ", IP source : " + IP_text + ", already there ? " + alrdythere);
            chat_control.perform_connection_back(remote_user, IP_text, alrdythere);
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(ChatNI.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public void from_disconnection(String remote_user, String IP_text) {
        chat_control.perform_disconnection(remote_user, IP_text);
    }
    
    public void from_receive_text(String remote_user, String txt, String IP_text) {
        chat_control.perform_send(remote_user, txt, IP_text);
    }
    
    
    
    public String get_user() {
        return username;
    }
    
    @Override
    public void update(Observable o, Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
