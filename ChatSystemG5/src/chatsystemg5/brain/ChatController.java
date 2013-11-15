package chatsystemg5.brain;
import chatsystemg5.common.*;
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
    
    ChatGUI chatGUI;
    MessageEmissionNI emissionNI;
    Thread receptionNI;
    
    public ChatController () {
        // initialize view
        // TO DO create view at the end
        chatGUI = new ChatGUI(this);
        
        // initialize model
        listDB = new ListModel(this);            
    }
    
    /**************** Controller init ****************/
    public void init_controller (String username) {
        // On crée la BDD User
        userDB = new UserModel(username);
        
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
        Hello msg = new Hello(username, false);
        InetAddress IP_dest = emissionNI.get_broadcast();
        //System.out.println("ChatController : " + IP_dest);
        this.emissionNI.send(msg, IP_dest);
        // create emmetor part of the app
    }
    
    // Connexion d'un autre user
     public void perform_connection (Hello hi, InetAddress IP_source) {
        // add him to the user list
        // equivalent of setState()
        listDB.add_user(hi.getUsername(), IP_source.getHostAddress());
        listDB.notifyObservers();

        
        // if remote user first connection
        if(!hi.isAck()){
            // create a new message to sent back ack
            Hello msg = new Hello(this.userDB.get_username(), true);
            this.emissionNI.send(msg, IP_source);
            
                    // notify the Observer (ListWindow) of the change about ModelList
        
        }
        
    }
    
     
    /**************** Disonnection ****************/
     
    // Déconnection du local user
    public void perform_disconnection () {
        Goodbye msg = new Goodbye(this.userDB.get_username());
        InetAddress IP_dest = emissionNI.get_broadcast();
        this.emissionNI.send(msg, IP_dest);
        // create emmetor part of the app
     }
    
    // Déconnection d'un autre user
    public void perform_disconnection (Goodbye bye, InetAddress IP_addr) {
        // remove the user who disconnected from the list
        // equivalent of setState()
        listDB.remove_user(bye.getUsername(), IP_addr.toString());
        listDB.notifyObservers();
    }
    
    /**************** Communication by text ****************/
    
    // Envoi d'un messsage texte
    public void perform_send (String username_and_IP, String text) {
        try {
            Text msg = new Text(this.userDB.get_username(), text);
            // On récupère l'@IP associée à l'username
            String IP_text = this.listDB.get_IP_addr(username_and_IP);
            // On transforme l'@IP de String à InetAddress
            InetAddress IP_dest = InetAddress.getByName(IP_text);
            emissionNI.send(msg, IP_dest);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Réception d'un message
    public void perform_send (Text txt, InetAddress IP_addr) {
        //display_message ()
        System.out.println(txt.toString());
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
