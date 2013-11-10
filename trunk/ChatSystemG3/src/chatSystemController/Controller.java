/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

import runChat.ChatSystem;

/* note pour plus tard : 
 *  - checkConnection a implementer
 *  - ecrire la javadoc et les commentaires
 */

/**
 * @author joanna
 *
 */
public class Controller {
	
	public void performConnect(String username){
		ChatSystem.getModelUsername().setUsername(username);
		ChatSystem.getChatNI().connect(username, true);
	}
	
	public void performDisconnect(String username){
		ChatSystem.getChatNI().disconnect(username);
		ChatSystem.getModelListUsers().clearListUsers();
	}
	
	public void performSendText (String text, ArrayList<String> recipientList){
		Iterator<String> it = recipientList.iterator();
		String recipient;
		ChatSystem.getModelText().setTextToSend(text);
		while (it.hasNext()){
			recipient = ((String)it.next());
			ChatSystem.getModelGroupRecipient().addRecipient(recipient);
		}
		ChatSystem.getChatNI().sendText();
	}
	
	public void messageReceived(String text, String username){
		ChatSystem.getModelText().setTextReceived(text);
		ChatSystem.getModelText().setRemote(username);
		System.out.println (username + " : " + text);
	}
	
	public void connectReceived(String username,InetAddress ipRemote, boolean ack){
		// si ack = false c'est juste une reponse a un hello 
		if (ack == false){
			//si state = disconnected on le passe a connected
			if (ChatSystem.getModelStates().isConnected() == false){
				ChatSystem.getModelStates().setState(true);
				System.out.println(ChatSystem.getModelUsername().getUsername() + " : connection succeed");
			}
			// on ajoute le remote a la liste
			ChatSystem.getModelListUsers().addUsernameList(username, ipRemote);
		// si ack = true c'est une demande de connexion	
		}else{
			System.out.println(username + " est connecté");
			// si c'est un nouvel utilisateur on l'ajoute et on repond
			if (ChatSystem.getModelListUsers().isInListUsers(username)){
				ChatSystem.getModelListUsers().addUsernameList(username, ipRemote);			
			}
			ChatSystem.getChatNI().connect(ChatSystem.getModelUsername().getUsername(),false);
		}
	}
	public void disconnectReceived(String username){
		ChatSystem.getModelListUsers().removeUsernameList(username);
		
	}
}
