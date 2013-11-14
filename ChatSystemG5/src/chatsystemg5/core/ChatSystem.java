package chatsystemg5.core;

import chatsystemg5.network.*;
import chatsystemg5.common.*;
import chatsystemg5.brain.*;

public class ChatSystem {
	
	public static void main (String[] args) {
            
            ChatController brain = new ChatController("BLT-DFT");
            
            // a changer en raison des modifications apporter au UserModel
           double i = 0.0; 
           while(true){
               if(i == 1000000000.0){
                    brain.perform_connection(brain.get_userDB().get_username());
                    i = 0;
               }
               else i++;
           }
            
//            Hello bonjour = new Hello("Joffrey", false);
//            
//            //test for an hello emission on localhost
//            Thread receive = new Thread(new MessageReceptionNI());
//            receive.setName("receive");
//            receive.start();
//            
//            Thread sender;
//            sender = new Thread(new MessageEmissionNI(bonjour));
//            sender.setName("send");
//            sender.run();
            
            //MessageEmissionNI msg_rcv = new MessageEmissionNI("boobs");
            //while(true) {
            //msg_rcv.run();
            //}
        
        }

}
