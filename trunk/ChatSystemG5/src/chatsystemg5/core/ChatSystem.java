    package chatsystemg5.core;

import chatsystemg5.network.*;
import chatsystemg5.common.*;
import chatsystemg5.brain.*;
import chatsystemg5.ihm.ConnectionWindow;

public class ChatSystem {
	
	public static void main (String[] args) {
            
            double i=0;
            ChatController brain = new ChatController();
            ChatController brain2 = new ChatController();
            while (i<1000000000) i++;
            System.out.println("Ready to send text");
            brain.perform_send("test@127.0.1.1", "Salut test");
            // a changer en raison des modifications apporter au UserModel
           
      
           //  a changer en raison des modifications apporter au UserModel

        }

}
