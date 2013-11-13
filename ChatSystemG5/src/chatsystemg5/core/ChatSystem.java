package chatsystemg5.core;

import chatsystemg5.network.*;

public class ChatSystem {
	
	public static void main (String[] args) {
        
            // test for an hello emission on localhost
            Thread receive = new Thread(new MessageReceptionNI());
            receive.setName("nom1");
            receive.start();
            
            Thread send = new Thread(new MessageEmissionNI());
            send.setName("nom2");
            send.start();
            

        
        }

}
