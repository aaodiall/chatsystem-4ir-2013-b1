package chatsystemg5.core;

import chatsystemg5.network.*;
import chatsystemg5.common.*;
import chatsystemg5.brain.*;

public class ChatSystem {
	
	public static void main (String[] args) {
            
            ChatController brain = new ChatController("BLT-DFT");
            // a changer en raison des modifications apporter au UserModel
           
      
           //  a changer en raison des modifications apporter au UserModel
//           double i = 0.0; 
//           while(true){
//               if(i == 1000000000.0){
                    brain.perform_connection(brain.get_userDB().get_username());
                    brain.perform_connection("BLT");
                    brain.perform_connection("DFT");

//                    i = 0;
//               }
//               else i++;
//           }

        
        }

}
