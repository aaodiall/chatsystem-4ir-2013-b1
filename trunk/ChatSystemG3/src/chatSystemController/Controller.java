/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import chatSystemModel.*;
import runChat.ChatSystem;

/* note pour plus tard : 
 *  - checkConnection a implementer
 *  - ecrire la javadoc et les commentaires
 */

/**
 * @author joanna
 * Le controller est le seul a ecrire dans le modele, c'est lui qui le met a jour
 * Il ne fait pas appel a ChatNI ni a ChatGUI (les views)
 * C'est au niveau des "update" que les envois se declenchent
 */
public class Controller {
	
	/* ATTRIBUTS A METTRE
	 * ModelFile modelFile;
	 * ModelFileInformation modelFileInformation;
	 * ModelGroupRecipient modelGroupRecipient;
	 * ModelListUsers modelListUsers;
	 * ModelStates modelStates;
	 * ModelText modelText;
	 * ModelUsername modelUsername;
	 * 
	 * METHODES A IMPLEMENTER
	 * public void performConnect(String username);
	 * public void performDisconnect(String username);
	 * public void performSendText(String text, ArrayList<String> recipientList);
	 * public void performSendFile(String recipient, String fileName);
	 * public void textReceived(String text, String username);
	 * public void connectReceived(String username,InetAddress ipRemote, boolean ack);
	 * public void disconnectReceived(String username)
	 * public void confirmationFileReceived(String recipient, String fileName);
	 * public void demandFileReceived(String sender, String fileName,String extension,int size);
	 * public void cancelFileReceived(String sender, int idDemand);
	*/
	
	private ModelListUsers modelListUsers;
	private ModelStates modelStates;	
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
			ModelStates modelStates,
			ModelText modelText,
			ModelUsername modelUsername, ModelGroupRecipient modelGroupRecipient) {
		this.modelListUsers = modelListUsers;
		this.modelStates = modelStates;
		this.modelText = modelText;
		this.modelUsername = modelUsername;
		this.modelGroupRecipient = modelGroupRecipient;
	}
	

	public void performConnect(String username){
		modelUsername.setUsername(username);
		//ChatSystem.getChatGui().getwCommunicate().setVisible(true);
		ChatSystem.getChatNI().connect(username, false);
		ChatSystem.getChatGui().getwConnect().setTfdUsername("");;
		System.out.println(modelUsername.getUsername() + " : connection succeed");
	}
	
	
	public void performDisconnect(){
		ChatSystem.getChatNI().disconnect(modelUsername.getUsername());
		modelListUsers.clearListUsers();;
		modelStates.setState(false);
		//ChatSystem.getChatGui().getwCommunicate().setVisible(false);
		//ChatSystem.getChatGui().getwConnect().setVisible(true);
		
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
		ChatSystem.getChatNI().sendMsgText(recipientList, text);
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
			if (modelStates.isConnected() == false){
				modelStates.setState(true);
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
			ChatSystem.getChatNI().connect(ChatSystem.getModelUsername().getUsername(),false);
		}
	}
	public void disconnectReceived(String username){
		modelListUsers.removeUsernameList(username);
		
	}
}