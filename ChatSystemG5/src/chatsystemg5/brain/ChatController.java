package chatsystemg5.brain;
import chatsystemg5.common.*;
import chatsystemg5.ihm.ChatGUI;
import chatsystemg5.network.*;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatController {

    UserModel userDB;
    ListModel listDB;
    
    ChatGUI chatGUI;
    MessageEmissionNI emissionNI;
    Thread receptionNI;
    
    public ChatController (String username) {
        // initialize view
        chatGUI = new ChatGUI(this);
        
        // initialize model
        userDB = new UserModel(username);
        listDB = new ListModel(this);
        
        // initialize network
            // create receptor part of the app
            this.receptionNI = new Thread(new MessageReceptionNI(this));
            // strat listenning the network on port 16000
            receptionNI.start();
            
            this.emissionNI = new MessageEmissionNI(this.userDB.get_username()); 
       

    }
    
    /**************** Connection ****************/
    
    // Connexion du local user
    public void perform_connection (String username) {
        try {
            Hello msg = new Hello(username, false);
            InetAddress IP_dest = InetAddress.getByName("255.255.255.255");
            this.emissionNI.send(msg, IP_dest);
            // create emmetor part of the app
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Connexion d'un autre user
     public void perform_connection (Hello hi, InetAddress IP_source) {
        // add him to the user list
        listDB.add_user(hi.getUsername(), IP_source.toString());
        // notify the Observer (ListWindow) of the change about ModelList
        listDB.setState();
        listDB.notifyObservers();
         
        // if remote user first connection
        if(!hi.isAck()){
            // create a new message to sent back ack
            Hello msg = new Hello(this.userDB.get_username(), true);
            this.emissionNI.send(msg, IP_source);
        }
        
    }
    
     
    /**************** Disonnection ****************/
     
    // Déconnection du local user
    public void perform_disconnection () {
        try {
            Goodbye msg = new Goodbye(this.userDB.get_username());
            InetAddress IP_dest = InetAddress.getByName("255.255.255.255");
            this.emissionNI.send(msg, IP_dest);
            // create emmetor part of the app
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Déconnection d'un autre user
    public void perform_disconnection (Goodbye bye, InetAddress IP_addr) {
        listDB.remove_user(bye.getUsername(), IP_addr.toString());
    }
    
    /**************** Communication by text ****************/
    
    // Envoi d'un messsage texte
    public void perform_send (String text) {
        Text msg = new Text(this.userDB.get_username(), text);
        //emissionNI.send(msg)
    }
    
    // Réception d'un message
    public void perform_send (Text txt, InetAddress IP_addr) {
        // plus tard
    }
    
    // getters
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
