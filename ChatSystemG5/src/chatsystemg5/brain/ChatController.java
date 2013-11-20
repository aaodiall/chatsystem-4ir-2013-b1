package chatsystemg5.brain;
import chatSystemCommon.*;
import chatsystemg5.ihm.ChatGUI;
import chatsystemg5.ihm.ChatWindow;
import chatsystemg5.ihm.ConnectionWindow;
import chatsystemg5.network.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatController {

    UserModel userDB;
    ListModel listDB;
    ConversationModel convDB;
    String username;
    
    ChatGUI chatGUI;
    MessageHandlerNI msg_handler;
    
    public ChatController () {
        // initialize view
        // TO DO create view at the end
        chatGUI = new ChatGUI(this);
        
        // initialize model
        listDB = new ListModel(this);
        convDB = new ConversationModel(this);
        
    }
    
    /**************** Controller init ****************/
    public void init_controller (String username) {
        // On crée la BDD User
        userDB = new UserModel(username);
        username = this.userDB.get_username();
        
        // initialize network
        msg_handler = new MessageHandlerNI(this);
    }
    
    
    /**************** Connection ****************/
    
    // Connexion du local user
    public void perform_connection () {
        msg_handler.send_connection(false);
    }
    
    // Connexion d'un autre user
     public void perform_connection (String r_user, String IP_text, Boolean alrdythere) {
        // Ajouter utilisateur au model
        listDB.add_user(r_user, IP_text);
        listDB.notifyObservers();
   
        // if remote user first connection
        if(!alrdythere){
            // create a new message to sent back ack
            msg_handler.send_connection(true);       
        }
     }
    
     
    /**************** Disonnection ****************/
     
    // Déconnection du local user
    public void perform_disconnection () {
        msg_handler.send_disconnection();
     }
    
    // Déconnection d'un autre user
    public void perform_disconnection (String remote_user, String IP_text) {
        // remove the user who disconnected from the list
        listDB.remove_user(remote_user, IP_text);
        listDB.notifyObservers();
    }
    
    /**************** Communication by text ****************/
    
    // Envoi d'un messsage texte
    public void perform_send (String username_and_IP, String text) {
        msg_handler.send_text(username_and_IP, text);
    }
    
    // Réception d'un message
    public void perform_send (String remote_user, String txt, String IP_text) {
        //display_message ()
        System.out.println("I'm Controller, receiving : " + txt.toString());
        //convDB.add_conversation(remote_user, IP_text, txt);        
    }
        
    /**************** Getters ****************/
    public UserModel get_userDB(){
        return this.userDB;
    }
    
    public ListModel get_listDB(){
        return this.listDB;
    }
 
    public ChatGUI get_chatGUI(){
        return this.chatGUI;
    }
    
}
