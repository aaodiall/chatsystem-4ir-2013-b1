/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

import chatSystemModel.*;
import runChat.ChatSystem;



/**
 * @author joanna
 *
 */
public class Controller {
	private ModelListUsers modelListUsers;
	private ModelStateConnected modelStateConnected;

	
	private ModelText modelText;
	private ModelUsername modelUsername;
	private ModelGroupRecipient modelGroupRecipient;
	

	
	/**
	 * @param modelListUsers
	 * @param modelStateConnected
	 * @param modelStateDisconnected
	 * @param modelText
	 * @param modelUsername
	 * @param modelGroupRecipient
	 */
	public Controller(ModelListUsers modelListUsers,
			ModelStateConnected modelStateConnected,
			ModelText modelText,
			ModelUsername modelUsername, ModelGroupRecipient modelGroupRecipient) {
		this.modelListUsers = modelListUsers;
		this.modelStateConnected = modelStateConnected;
		this.modelText = modelText;
		this.modelUsername = modelUsername;
		this.modelGroupRecipient = modelGroupRecipient;
	}

	

	public void performConnect(String username){
		modelUsername.setUsername(username);
		ChatSystem.getChatGui().getwCommunicate().setVisible(true);
		ChatSystem.getChatNI().sendHello(username, false);
		ChatSystem.getChatGui().getwConnect().setTfdUsername("");;
	}
	
	
	public void performDisconnect(){
		ChatSystem.getChatNI().sendBye(modelUsername.getUsername());
		modelListUsers.clearListUsers();;
		modelStateConnected.setState(false);
		ChatSystem.getChatGui().getwCommunicate().setVisible(false);
		ChatSystem.getChatGui().getwConnect().setVisible(true);
		
		System.out.println(modelUsername.getUsername() + " : deconnection succeed");
	}
	
	public void performSendText (String text, ArrayList<String> recipientList){
		Iterator<String> it = recipientList.iterator();
		String recipient;
		modelText.setTextToSend(text);
		while (it.hasNext()){
			recipient = ((String)it.next());
			modelGroupRecipient.addRecipient(recipient);
		}
		ChatSystem.getChatNI().sendText();
	}
	
	public void messageReceived(String text, String username){
		modelText.setTextReceived(text);
		modelText.setRemote(username);
		System.out.println (username + " : " + text);
	}
	
	
	public void connectReceived(String username,InetAddress ipRemote, boolean ack){
		// si ack = false c'est juste une reponse a un hello 
		if (ack == false){
			//si state = disconnected on le passe a connected
			if (modelStateConnected.isConnected() == false){
				modelStateConnected.setState(true);
				System.out.println(modelUsername.getUsername() + " : connection succeed");
				ChatSystem.getChatGui().getwConnect().setVisible(false);
				
			}
			// on ajoute le remote a la liste
			modelListUsers.addUsernameList(username, ipRemote);
			
		// si ack = true c'est une demande de connexion	
		}else{
			System.out.println(username + " est connecté");
			// si c'est un nouvel utilisateur on l'ajoute et on repond
			if (modelListUsers.isInListUsers(username)){
				modelListUsers.addUsernameList(username, ipRemote);
			
			}
			ChatSystem.getChatNI().sendHello(modelUsername.getUsername(),false);
		}
	}
	public void disconnectReceived(String username){
		modelListUsers.removeUsernameList(username);
		
	}
}