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
	private ModelFileInformation modelFileInformation;
	private ModelFile modelFile;
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
		this.chatNI.connect(false);
		System.out.println( this.modelUsername.getUsername() + " : connected");
		// affichage graphique de la fenêtre de communication
		/*this.chatgui.getwConnect().setVisible(false);
		this.chatgui.getwCommunicate().setVisible(true);*/
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
		
	}
	
	public void performDisconnect(){
		if (this.modelStates.isConnected()){
			// on passe l'etat a deconnecte
			this.modelStates.setState(false);
			// on reinitialise la liste des utilisateurs distants
			this.modelListUsers.clearListUsers();
			// on lance la deconnexion
			this.chatNI.disconnect();
			/*this.chatgui.getwCommunicate().setVisible(false);
			this.chatgui.getwConnect().setVisible(true);*/
			System.out.println(this.modelUsername.getUsername() + " : disconnected");
		}
	}

	public void performAddURecipient(String username){
		if (this.modelStates.isConnected()){
			this.modelGroupRecipient.addRecipient(username);
		}
	}

	public void performRemoveRecipient(String username){
		if (this.modelStates.isConnected()){
			this.modelGroupRecipient.removeRecipient(username);
		}
	}
	
	public void performSendText (String text){
		/*Iterator<String> it = recipientList.iterator();
		String recipient;
		modelText.setTextToSend(text);
		while (it.hasNext()){
			recipient = it.next();
			modelGroupRecipient.addRecipient(recipient);
		}*/
		if (this.modelStates.isConnected()){
			InetAddress ipRecipient;
			for(int i=0; i < this.modelGroupRecipient.getGroupRecipients().size();i++){
				ipRecipient=this.modelListUsers.getListUsers().get(this.modelGroupRecipient.getGroupRecipients().poll());
				this.chatNI.sendMsgText(ipRecipient, text);
			}
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
		// il faut récupérer l'ip correspondant au username
		this.modelFileInformation = new ModelFileInformation();
		String fileName = this.modelFileInformation.getName();
		long size = this.modelFileInformation.getSize();
		//this.chatNI.sendPropositionFile(username, fileName, size);
	}
	
	/**
	 * Permet d'envoyer la réponse de l'utilisateur à une demande d'envoi de fichier
	 */
	public void performFileAnswer(boolean answer){
		
	}
	
	/**
	 * Permet de signaler à l'utilisateur une demande d'envoi de fichier
	 */
	public void filePropositionReceived(String remote, String file, long size){
		System.out.println("Do you accept to receive the file " + file + " from "+ remote +" ?");
		System.out.println("size of file : " + size);
	}
	
	/**
	 * Permet de signaler à l'utilisateur la réponse de l'utilisateur distant
	 */
	public void fileAnswerReceived(String remote,int idDemand,boolean isAccepted){
		if (isAccepted){
			System.out.println(remote + "has accepted your file");
			System.out.println("Transfert in progress");
		}else{
			System.out.println(remote + "has refused your file");
		}
	}
	
	/**
	 * permet d'arrêter un téléchargement en cours
	 */
	public void fileTranfertCancelReceived(String remote, int idDemand){
		System.out.println(remote + "has cancelled the transfert");
	}
	
	public void messageReceived(String text, String username){
		if (modelStates.isConnected()){
			this.modelText.setTextReceived(text);
			this.modelText.setRemote(username);
			System.out.println (username + " : " + text);
		}
	}
	
	public void connectReceived(String username,InetAddress ipRemote, boolean ack){		
		if (modelStates.isConnected()){
			// si ack = false c'est une demande de connexion donc on repond	
			if (!ack){
				this.chatNI.connect(true);
			}
			if ((!modelListUsers.isInListUsers(username)) ){					
				this.modelListUsers.addUsernameList(username, ipRemote);
				System.out.println(username + " s'est connecté");
			}	
		}
	}
	
	public void disconnectReceived(String username){
		if (modelStates.isConnected()){
			this.modelListUsers.removeUsernameList(username);
			System.out.println(username + " s'est deconnecté");	
		}			
	}
}