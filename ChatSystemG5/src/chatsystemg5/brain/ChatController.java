package chatsystemg5.brain;
import chatsystemg5.common.*;
import chatsystemg5.network.*;

public class ChatController {

    UserModel userDB;
    ChatNI chtNI;
    MessageEmissionNI emissionNI;
    
    public ChatController (String username) {
        userDB = new UserModel(username);
        emissionNI = new MessageEmissionNI(username);
    }
    
    public void perform_connection (String username) {
        Hello msg = new Hello(username, false);
        emissionNI.send(msg);             
    }
    
     public void perform_connection (Hello hi) {
        String user_connected = hi.getUsername();
        
    }
    
    public void perform_disconnection () {
        Goodbye msg = new Goodbye(username);
        emissionNI.send(msg);
    }
    
    public void perform_send (String text) {
        Text msg = new Text(username, text);
        emissionNI.send(msg);
    }
    
}
