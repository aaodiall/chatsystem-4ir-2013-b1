/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
	

	public void performConnect(String username) throws UnknownHostException{
		modelUsername.setUsername(username);
		// l'utilisateur est connecte
		this.modelStates.setState(true);
		/*ChatSystem.getChatGui().getwCommunicate().setVisible(true);
		ChatSystem.getChatNI().connect(username, true);
		ChatSystem.getChatGui().getwConnect().setTfdUsername("");;*/
		System.out.println(modelUsername.getUsername() + " : connection in progress");
		//juste pour tester implementation de update pour liste users
		modelListUsers.addUsernameList("jojo@2.2.2.2", InetAddress.getLocalHost());
	}
	
	
	public void performDisconnect(){
		modelStates.setState(false);
		modelListUsers.clearListUsers();
		//ChatSystem.getChatNI().disconnect(modelUsername.getUsername());
		//ChatSystem.getChatGui().getwCommunicate().setVisible(false);
		//ChatSystem.getChatGui().getwConnect().setVisible(true);
		
		System.out.println(modelUsername.getUsername() + " : deconnection in progress");
	}
	
	public void performSendText (String text, ArrayList<String> recipientList){
		Iterator<String> it = recipientList.iterator();
		String recipient;
		modelText.setTextToSend(text);
		while (it.hasNext()){
			recipient = ((String)it.next());
			modelGroupRecipient.addRecipient(recipient);
		}
		ChatSystem.getChatNI().sendMsgText(recipientList, text,this.modelUsername.getUsername());
	}
	
	public void messageReceived(String text, String username){
		modelText.setTextReceived(text);
		modelText.setRemote(username);
		System.out.println (username + " : " + text);
	}
	
	public void connectReceived(String username,InetAddress ipRemote, boolean ack){
		//ChatSystem.getChatGui().getwConnect().setVisible(false);
		// si ack = true c'est une demande de connexion donc on repond	
		if (ack=true){
			if ((!modelListUsers.isInListUsers(username)) && modelStates.isConnected()){
				ChatSystem.getChatNI().connect(ChatSystem.getModelUsername().getUsername(),false);
				modelListUsers.addUsernameList(username, ipRemote);
				
			}	
		}
		else{
			if ((!modelListUsers.isInListUsers(username)) && (modelStates.isConnected() == true)){
				modelListUsers.addUsernameList(username, ipRemote);
				
			}
			
		}
		
	}
	public void disconnectReceived(String username){
		modelListUsers.removeUsernameList(username);
		System.out.println(username + " est deconnecté");		
	}
}