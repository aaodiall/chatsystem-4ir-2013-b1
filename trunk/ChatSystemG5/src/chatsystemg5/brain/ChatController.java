package chatsystemg5.brain;
import chatsystemg5.common.*;
import chatsystemg5.network.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatController {

    UserModel userDB;
    ListModel listDB;
    ChatNI chtNI;
    MessageEmissionNI emissionNI;
    Thread receptionNI;
    
    public ChatController (String username) {
        userDB = new UserModel(username);
        listDB = new ListModel();
        
        // create receptor part of the app
        //Thread receptionNI = new Thread(new MessageReceptionNI());
        
        // strat listenning the network on port 16000
        receptionNI.start();
        
        // create emmetor part of the app
        try {
            emissionNI = new MessageEmissionNI(this.userDB.get_username());
        } catch (SocketException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**************** Connection ****************/
    
    // Connexion du local user
    public void perform_connection (String username) {
        Hello msg = new Hello(username, false);
        emissionNI.send(msg);             
    }
    
    // Connexion d'un autre user
     public void perform_connection (Hello hi, InetAddress IP_addr) {
        listDB.add_user(hi.getUsername(), IP_addr.toString());
    }
    
     
    /**************** Disonnection ****************/
     
    // Déconnection du local user
    public void perform_disconnection () {
        Goodbye msg = new Goodbye(this.userDB.get_username());
        emissionNI.send(msg);
    }
    
    // Déconnection d'un autre user
    public void perform_disconnection (Goodbye bye, InetAddress IP_addr) {
        listDB.remove_user(bye.getUsername(), IP_addr.toString());
    }
    
    /**************** Communication by text ****************/
    
    // Envoi d'un messsage texte
    public void perform_send (String text) {
        Text msg = new Text(this.userDB.get_username(), text);
        emissionNI.send(msg);
    }
    
    // Réception d'un message
    public void perform_send (Text txt, InetAddress IP_addr) {
        // plus tard
    }
    
}
