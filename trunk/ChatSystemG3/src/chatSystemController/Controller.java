/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
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
	private HashMap <Integer,ModelFileToSend> filesToSend;
	private HashMap <Integer,ModelFileToReceive> filesToReceive;
	private HashMap <String,Integer> remoteToDemand;
	private long maxSizeToTransfer;
	private long nbSendingBytes;
	private long nbReceivingBytes;
	private int currentidSendDemand;
	private int maxidDemand;
	private ArrayBlockingQueue <ModelFileToSend> ftoSend;
	
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
		this.ftoSend = new ArrayBlockingQueue<ModelFileToSend>(this.numFileMax,true);
		this.maxRead = 1024;
		this.maxWrite = 1024;
		this.currentidSendDemand = 0;
		this.maxidDemand = 50; // on suppose que quelqu'un n'enverra pas  plus 50 fichiers dans une seule session
		this.filesToSend = new HashMap <Integer,ModelFileToSend> ();
		this.filesToReceive = new HashMap<Integer,ModelFileToReceive> ();
		this.remoteToDemand = new HashMap <String,Integer>();
		this.maxSizeToTransfer = 2000000000;
		this.nbReceivingBytes = 0;
		this.nbSendingBytes = 0;
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
		// on lance le thread du controller pour les fichiers
		this.start();
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
		this.ftoSend.add(f);
	}
	
	/**
	 * Permet de proposer le fichier choisi a une personne connectée
	 * @param username
	 */
	public void performPropositionFile(String remote, String filePath){
		// on cree le modelFile
		ModelFileToSend f = new ModelFileToSend (remote,filePath,this.currentidSendDemand,this.maxRead);
		this.nbSendingBytes += f.getSize();
		if ((this.filesToSend.size() <= this.numFileMax) && (this.nbSendingBytes<=this.maxSizeToTransfer)){
			//on l'ajoute a la liste des fichier a envoyer;
			this.filesToSend.put(f.getIdDemand(), f);
			f.addObserver(chatgui);
			if (this.currentidSendDemand <= this.maxidDemand){
				this.currentidSendDemand++;
			}else{
				this.currentidSendDemand = 0;
			}
			//On recupere l'ip du destinataire
			InetAddress ipRemote = this.modelListUsers.getListUsers().get(remote);
			// on envoie la proposition
			this.chatNI.sendPropositionFile(remote, ipRemote, f.getName(), f.getSize(),f.getIdDemand());
		}else{
			// prevenir l'interface graphique
			System.out.println("Too many files to send, try later");
		}
	}

	
	/**
	 * Permet d'envoyer la réponse de l'utilisateur à une demande d'envoi de fichier
	 */
	public void performFileAnswer(String remote, boolean answer){
		ModelFileToReceive f = this.filesToReceive.get(this.remoteToDemand.get(remote));
		InetAddress ipRemote = this.modelListUsers.getListUsers().get(remote);
		if (answer == true){
			// si l'utilisateur veut accepter plus que le maximum autorise on ne reçoit pas le fichier
			if (this.nbReceivingBytes + f.getSize() > this.maxSizeToTransfer){
				f.deleteFile();
				f.deleteObservers();
				this.filesToReceive.remove(this.remoteToDemand.get(remote));
				//PREVENIR LA GUI
				System.err.println("attemp of sending more than 2Go, file not sent");
			}else{
				this.nbReceivingBytes += f.getSize();
				this.chatNI.sendConfirmationFile(remote,ipRemote, f.getName(), answer, f.getIdDemand());
			}
		}else{
			this.chatNI.sendConfirmationFile(remote,ipRemote, f.getName(), answer, f.getIdDemand());
			f.deleteFile();
			f.deleteObservers();
			this.filesToReceive.remove(this.remoteToDemand.get(remote));
		}
	}
	
	/**
	 * Permet de signaler à l'utilisateur une demande d'envoi de fichier
	 */
	public void filePropositionReceived(String remote, String file, long size, int idDemand){
		if (this.modelStates.isConnected()){
			ModelFileToReceive f = new ModelFileToReceive(remote,file,size,idDemand,this.maxWrite);
			f.addObserver(chatgui);
			System.out.println("Controller --> idDemand received : " + idDemand);
			System.out.println("Controller --> file demand received from " + remote);
			this.filesToReceive.put(idDemand, f);
			this.remoteToDemand.put(remote, idDemand);
			f.setStateReceivedDemand(true);
		}
	}
	
	/**
	 * Permet de signaler à l'utilisateur la réponse de l'utilisateur distant
	 */
	public void fileAnswerReceived(String remote,int idDemand,boolean isAccepted){
		System.out.println("file answer received from " + remote);
		if (this.modelStates.isConnected()){
			ModelFileToSend f = this.filesToSend.get(idDemand);
			if (isAccepted){				
				this.performJoinFile(f);
			}else{
				f.setRefused();
				f.deleteObservers();
				this.filesToSend.remove(idDemand);
			}
		}
	}
	
	/**
	 * permet d'arrêter un téléchargement en cours
	 */
	public void fileTranfertCancelReceived(String remote, int idDemand){}
	
	public void fileReceived(int idDemand){
		this.filesToReceive.remove(idDemand);
	}
	
	public void messageReceived(String text, String username){
		if (modelStates.isConnected() && this.modelListUsers.isInListUsers(username)){
			this.modelText.setRemote(username);
			this.modelText.setTextReceived(text);
			System.out.println ("message received   "+username + " : " + text);
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
	
	public void partReceived(byte[] fileBytes,int idDemand, boolean isLast){	
		this.filesToReceive.get(idDemand).writeFilePart(fileBytes, isLast);
	}
	
	/**
	 * sert à envoyer les fichiers
	 */
	public void run(){
		while (this.modelStates.isConnected()){
			try{
				Thread.sleep(100);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			while (!this.ftoSend.isEmpty()){
				ModelFileToSend f = this.ftoSend.poll();
				int nbPartsSent = 0;
				byte[] part;
				while(nbPartsSent < (f.getNumberParts()-1)){
					part=f.readNextPart();
					this.chatNI.sendPart(part, f.getIdDemand(),false);
					nbPartsSent++;
				}
				part=f.readNextPart();
				this.chatNI.sendPart(part, f.getIdDemand(),true);		
				nbPartsSent++;
				// toutes les parties sont envoyees
				f.resetLevel();
			}
		}
	}
	
}