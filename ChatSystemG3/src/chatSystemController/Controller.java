/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import chatSystemIHMs.ChatGUI;
import chatSystemModel.*;
import chatSystemNetwork.ChatNI;

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
public class Controller extends Thread{

	private ModelListUsers modelListUsers;
	private ModelStates modelStates;	
	private ModelText modelText;
	private ModelUsername modelUsername;
	private ModelGroupRecipient modelGroupRecipient;
	private ChatGUI chatgui;
	private ChatNI chatNI;
	private int numFileMax;
	private int maxWrite;
	private int maxRead;
	private ArrayList <ModelFileToSend> filesToSend;
	private HashMap <Integer,ModelFileToReceive> filesToReceive;
	private int numReceiveDemands;
	private int numSendDemands;
	
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
		this.numFileMax = 5;
		this.maxRead = 1024;
		this.maxWrite = 1024;
		this.filesToSend = new ArrayList<ModelFileToSend> (this.numFileMax);
		this.filesToReceive = new HashMap<Integer,ModelFileToReceive> (this.numFileMax);
		this.numReceiveDemands = 0;
		this.numSendDemands = 0;
		this.start();
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
	}
	
	public void performDisconnect(){
		if (this.modelStates.isConnected()){
			// on passe l'etat a deconnecte
			this.modelStates.setState(false);
			// on reinitialise la liste des utilisateurs distants
			this.modelListUsers.clearListUsers();
			// on lance la deconnexion
			this.chatNI.disconnect();
			System.out.println(this.modelUsername.getUsername() + " : disconnected");
		}
	}

	public void performAddURecipient(String remote){
		if (this.modelStates.isConnected()){
			this.modelGroupRecipient.addRecipient(remote);
		}
	}

	public void performRemoveRecipient(String remote){
		if (this.modelStates.isConnected()){
			this.modelGroupRecipient.removeRecipient(remote);
		}
	}
	
	public void performSendText (String remote,String text){
		/*Iterator<String> it = recipientList.iterator();
		String recipient;
		modelText.setTextToSend(text);
		while (it.hasNext()){
			recipient = it.next();
			modelGroupRecipient.addRecipient(recipient);
		}*/
		/*if (this.modelStates.isConnected()){
			InetAddress ipRecipient;
			for(int i=0; i < this.modelGroupRecipient.getGroupRecipients().size();i++){
				ipRecipient=this.modelListUsers.getListUsers().get(this.modelGroupRecipient.getGroupRecipients().poll());
				this.chatNI.sendMsgText(ipRecipient, text);
			}
		}*/
		InetAddress ipRecipient;
		ipRecipient=this.modelListUsers.getListUsers().get(remote);
		this.chatNI.sendMsgText(ipRecipient, text);
		
	}
	
	/**
	 * Demande à la GUI de lancer le processus de récupération de choix de fichier à envoyer
	 */
	public void performJoinFile(ModelFileToSend f){		
		f.readFile();
		this.chatNI.sendMsgFile(f.getAllParts(), f.getIdDemand());
		this.filesToSend.remove(f.getIdDemand());
		this.numSendDemands--;
	}
	
	/**
	 * Permet de proposer le fichier choisi a une personne connectée
	 * @param username
	 */
	public void performPropositionFile(String remote, String filePath){
		if (this.numSendDemands < this.numFileMax){
			// on cree le modelFile
			ModelFileToSend f = new ModelFileToSend (remote,filePath,this.numSendDemands,this.maxRead);
			this.numSendDemands++;
			//on l'ajoute a la liste des fichier a envoyer;
			this.filesToSend.add(f);
			//On recupere l'ip du destinataire
			InetAddress ipRemote = this.modelListUsers.getListUsers().get(remote);
			// on envoie la proposition
			this.chatNI.sendPropositionFile(remote, ipRemote, f.getName(), f.getSize(),f.getIdDemand());
		}else{
			System.out.println("Too many files to send, try later");
		}
	}
	
	/**
	 * Permet d'envoyer la réponse de l'utilisateur à une demande d'envoi de fichier
	 */
	public void performFileAnswer(String remote, boolean answer){
		int i = 0;
		if ((answer == true) && (this.numReceiveDemands <= this.numFileMax)){
			InetAddress ipRemote = this.modelListUsers.getListUsers().get(remote);
			// recherche du bon model, on part du premier element du tableau
			boolean trouve = false;
			while (!trouve && i<this.filesToReceive.size()){
				ModelFileToReceive f = this.filesToReceive.get(i);
				System.out.println("file remote : " + f.getRemote() + " remote : " +remote);
				if(f.getRemote().equals(remote)){
					trouve = true;
					this.chatNI.sendConfirmationFile(remote,ipRemote, f.getName(), answer, f.getIdDemand(),f.getNumberParts());
				}
				i++;
			}
			if (trouve == false){
				System.out.println("file not found");
			}
		}else if (this.numReceiveDemands <= this.numFileMax){
			// prévenir l'interface graphique
			System.out.println("MaxFile atteint");
		}
	}
	
	/**
	 * Permet de signaler à l'utilisateur une demande d'envoi de fichier
	 */
	public void filePropositionReceived(String remote, String file, long size, int idDemand){
		if (this.modelStates.isConnected()){
			ModelFileToReceive f = new ModelFileToReceive(remote,file,size,idDemand,this.maxWrite);
			f.setStateReceivedDemand(true);
			this.filesToReceive.put(idDemand, f);
			// signale normalement a la gui qu'on a recu une demande
			this.chatgui.proposeFile(remote, file, size);
			System.out.println("file demand received from " + remote);
		}
	}
	
	/**
	 * Permet de signaler à l'utilisateur la réponse de l'utilisateur distant
	 */
	public void fileAnswerReceived(String remote,int idDemand,boolean isAccepted){
		System.out.println("file answer received from " + remote);
		if (this.modelStates.isConnected()){
			ModelFileToSend f = this.filesToSend.get(0);
			boolean trouve = false;
			if (isAccepted){
				//on cherche le fichier qui correspond
				while(!trouve && this.filesToSend.iterator().hasNext()){
					f = this.filesToSend.iterator().next();
					if (f.getIdDemand() == idDemand){
						trouve = true;
					}
				}
				this.performJoinFile(f);
			}else{
				System.out.println("file refused");
				this.filesToSend.remove(idDemand);
				this.numSendDemands--;
			}
		}
	}
	
	/**
	 * permet d'arrêter un téléchargement en cours
	 */
	public void fileTranfertCancelReceived(String remote, int idDemand){
		
	}
	
	public void messageReceived(String text, String username){
		if (modelStates.isConnected() && this.modelListUsers.isInListUsers(username)){
			this.modelText.setRemote(username);
			this.modelText.setTextReceived(text);
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
	
	public void partReceived(byte[] fileBytes, int idDemand, boolean isLast){
		System.out.println(" size read " + fileBytes.length);	
		if (isLast == false){
			this.filesToReceive.get(idDemand).writeFilePart(fileBytes, isLast);
		}else{
			this.filesToReceive.remove(idDemand);
			this.numReceiveDemands--;
		}
	}
	
	
	// check les connections
	public void run(){
		int n = 1;
		while(true){
			try{
				Thread.sleep(1000);
				if (this.filesToReceive.size() > 0){
					this.chatNI.checkReceives();
					System.out.println("check done");
				}
				if (n > 60){
					if (this.modelStates.isConnected()){
						this.chatNI.connect(false);
					}
					n=0;
				}
				n++;
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
}