    package chatsystemg5.core;

import chatsystemg5.network.*;
import chatsystemg5.brain.*;
import chatsystemg5.ihm.ConnectionWindow;

public class ChatSystem {
	
	public static void main (String[] args) {
            
            //double i=0;
            ChatController brain = new ChatController();
            
            /* Ne marche pas depuis le remodelage :
             * il y avait un problème de retour de connexion où l'adresse IP à qui
             * renvoyer le Hello n'était pas donnée, tentative pour la donner pas terrible.
             * Passage MsgHandler -> ChatNI -> ChatController problématique
             * alors que MsgHandler -> ChatController OK. Bizarre
             * Pb surtout au niveau du Thread, nullpointerexception
             */
            
            ChatController brain2 = new ChatController();
            /*while (i<1000000000) i++;
            System.out.println("Ready to send text");
            brain.perform_send("test@127.0.1.1", "Salut test");*/
            // a changer en raison des modifications apporter au UserModel
           
      
           //  a changer en raison des modifications apporter au UserModel

        }

}
