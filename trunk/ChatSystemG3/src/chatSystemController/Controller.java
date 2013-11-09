/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;

import runChat.ChatSystem;


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
		ChatSystem.getModelListUsers().clearListUsers();;
		
	}
	
	public void ConnectReceived(String username,InetAddress ipRemote, boolean ack){
		// si ack = false c'est juste une reponse a un hello 
		if (ack == false){
			//si state = disconnected on le passe a connected
			if (ChatSystem.getModelStates().isConnected() == false){
				ChatSystem.getModelStates().setState(true);
				System.out.println(ChatSystem.getModelUsername().getUsername() + " : connection succeed");
			}
			// on ajoute le remote a la liste
			ChatSystem.getModelListUsers().AddUsernameList(username, ipRemote);
		// si ack = true c'est une demande de connexion	
		}else{
			System.out.println(username + " est connecté");
			// si c'est un nouvel utilisateur on l'ajoute et on repond
			if (ChatSystem.getModelListUsers().IsInListUsers(username)){
				ChatSystem.getModelListUsers().AddUsernameList(username, ipRemote);
			
			}
			ChatSystem.getChatNI().connect(ChatSystem.getModelUsername().getUsername(),false);
		}
	}
	public void DisconnectReceived(String username){
		ChatSystem.getModelListUsers().removeusernameList(username);
		
	}
}
