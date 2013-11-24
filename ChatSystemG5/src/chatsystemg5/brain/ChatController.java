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

    private UserModel userDB;
    private ListModel listDB;
    private ConversationModel convDB;
    
    private String username;
    
    private ChatGUI chatGUI;
    private ChatNI chatNI;
    
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
        chatNI = new ChatNI(this);
    }
    
    
    /**************** Connection ****************/
    
    // Connexion du local user
    public void perform_connection () {
        chatNI.to_connection(username, false);

    }
    
    // Connexion d'un autre user
    public void perform_connection_back (String remote_user, String IP_text, Boolean alrdythere) {
        //System.out.println("I'm Controller, receiving connection : remote user : " +remote_user + ", IP source : " + IP_text + ", already there ? " + alrdythere);
        // Ajouter utilisateur au model
        listDB.add_user(remote_user, IP_text);

   
        // if remote user first connection
        if(!alrdythere){
            System.out.println("I'm Controller : sending back a Hello to : " + remote_user + " @ " + IP_text);
            // create a new message to sent back ack
            chatNI.to_connection(remote_user + "@" + IP_text, true);       
        }
     }
    
     
    /**************** Disonnection ****************/
     
    // Déconnection du local user
    public void perform_disconnection () {
        chatNI.to_disconnection();
     }
    
    // Déconnection d'un autre user
    public void perform_disconnection (String remote_user, String IP_text) {
        // remove the user who disconnected from the list
        listDB.remove_user(remote_user, IP_text);
    }
    
    /**************** Communication by text ****************/
    
    // Envoi d'un messsage texte
    public void perform_send (String username_and_IP, String text) {
        chatNI.to_send_text(username_and_IP, text);
    }
    
    // Réception d'un message
    public void perform_send (String remote_user, String txt, String IP_text) {
        //display_message ()
        String conversation = new String(txt + "\n[" + System.currentTimeMillis() + "]");
        convDB.add_conversation(remote_user, IP_text, conversation);        
    }
        
    /**************** Getters ****************/
    
    public UserModel get_userDB(){
        return this.userDB;
    }
    
    public ListModel get_listDB(){
        return this.listDB;
    }
    
    public ConversationModel get_convDB(){
        return this.convDB;
    }
 
    public ChatGUI get_chatGUI(){
        return this.chatGUI;
    }
    
}
