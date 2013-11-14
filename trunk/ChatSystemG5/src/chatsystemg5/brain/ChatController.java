package chatsystemg5.brain;
import chatsystemg5.common.*;
import chatsystemg5.network.*;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatController {

    UserModel userDB;
    ChatNI chtNI;
    MessageEmissionNI emissionNI;
    Thread receptionNI;
    
    public ChatController (String username) {
        userDB = new UserModel(username);
        
        // create receptor part of the app
        Thread receptionNI = new Thread(new MessageReceptionNI());
        
        // strat listenning the network on port 16000
        receptionNI.start();
        
        // create emmetor part of the app
        try {
            emissionNI = new MessageEmissionNI(this.userDB.get_username());
        } catch (SocketException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void perform_connection (String username) {
        Hello msg = new Hello(username, false);
        emissionNI.send(msg);             
    }
    
     public void perform_connection (Hello hi) {
        String user_connected = hi.getUsername();
        
    }
    
    public void perform_disconnection () {
        Goodbye msg = new Goodbye(this.userDB.get_username());
        emissionNI.send(msg);
    }
    
    public void perform_send (String text) {
        Text msg = new Text(this.userDB.get_username(), text);
        emissionNI.send(msg);
    }
    
}
