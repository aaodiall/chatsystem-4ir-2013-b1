/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import chatSystemIHMs.ChatGUI;
import chatSystemModel.*;
import chatSystemNetwork.ChatNI;
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
	private ChatGUI chatgui;
	private ChatNI chatNI;

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
	public ChatGUI getChatgui() {
		return chatgui;
	}


	public void setChatgui(ChatGUI chatgui) {
		this.chatgui = chatgui;
	}

	public void setChatNI(ChatNI chatNI) {
		this.chatNI = chatNI;
	}
	
	public void performConnect(String username){
		// enregistrement du pseudo
		this.modelUsername.setUsername(username);
		// passage dans l'etat connecte
		this.modelStates.setState(true);
		// lancement de la connexion
		this.chatNI.connect(username, false);
		// affichage graphique de la fenêtre de communication
		this.chatgui.getwConnect().setVisible(false);
		this.chatgui.getwCommunicate().setVisible(true);
		//test pour modelistusers update
		/*try {
			this.modelListUsers.addUsernameList("alpha", InetAddress.getLocalHost());
			this.modelListUsers.addUsernameList("alpha", InetAddress.getLocalHost());
			this.modelListUsers.addUsernameList("alpha", InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/		
		//ChatSystem.getChatGui().getwConnect().setTfdUsername("");
		System.out.println( "toto " +this.modelUsername.getUsername() + " : connected");
	}
	
	public void performDisconnect(){
		// on passe l'etat a deconnecte
		this.modelStates.setState(false);
		// on reinitialise la liste des utilisateurs distants
		this.modelListUsers.clearListUsers();
		// on lance la deconnexion
		this.chatNI.disconnect(this.modelUsername.getUsername());
		this.chatgui.getwCommunicate().setVisible(false);
		this.chatgui.getwConnect().setVisible(true);
		System.out.println(this.modelUsername.getUsername() + " : disconnected");
	}

	public void performAddURecipient(String username){
		this.modelGroupRecipient.addRecipient(username);
	}

	public void performRemoveRecipient(String username){
		this.modelGroupRecipient.removeRecipient(username);
	}

	public void performSendText (String text){
		/*Iterator<String> it = recipientList.iterator();
		String recipient;
		modelText.setTextToSend(text);
		while (it.hasNext()){
			recipient = it.next();
			modelGroupRecipient.addRecipient(recipient);
		}*/
		InetAddress ipRecipient;
		for(int i=0; i < this.modelGroupRecipient.getGroupRecipients().size();i++){
			ipRecipient=this.modelListUsers.getListUsers().get(this.modelGroupRecipient.getGroupRecipients().poll());
			chatNI.sendMsgText(ipRecipient, text,this.modelUsername.getUsername());
		}

	}
	
	/**
	 * Demande à la GUI de lancer le processus de récupération de choix de fichier à envoyer
	 */
	public void performJoinFile(){
		
	}
	
	/**
	 * Permet de proposer le fichier choisi a une personne connectée
	 * @param username
	 */
	public void performPropositionFile(String username){
		
	}
	
	/**
	 * Permet d'envoyer la réponse de l'utilisateur à une demande d'envoi de fichier
	 */
	public void performFileAnswer(){
		
	}
	
	/**
	 * Permet de signaler à l'utilisateur une demande d'envoi de fichier
	 */
	public void propositionFileReceived(){
		
	}
	
	/**
	 * Permet de signaler à l'utilisateur la réponse de l'utilisateur distant
	 */
	public void fileAnswerReceived(){
		
	}
	
	/**
	 * permet d'arrêter un téléchargement en cours
	 */
	public void fileTranfertCancelReceived(){
		
	}
	
	public void messageReceived(String text, String username){
		this.modelText.setTextReceived(text);
		this.modelText.setRemote(username);
		System.out.println (username + " : " + text);
	}
	
	public void connectReceived(String username,InetAddress ipRemote, boolean ack){
		//ChatSystem.getChatGui().getwConnect().setVisible(false);		
		if (modelStates.isConnected()){
			// si ack = false c'est une demande de connexion donc on repond	
			if (!ack){
				ChatSystem.getChatNI().connect(this.modelUsername.getUsername(),true);
			}
			if ((!modelListUsers.isInListUsers(username)) ){					
				modelListUsers.addUsernameList(username, ipRemote);
				System.out.println(username + " s'est connecté");
			}	
		}
	}
	
	public void disconnectReceived(String username){
		this.modelListUsers.removeUsernameList(username);
		System.out.println(username + " s'est deconnecté");		
	}
}