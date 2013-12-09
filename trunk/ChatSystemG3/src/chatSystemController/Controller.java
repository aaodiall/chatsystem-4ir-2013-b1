/**
 * 
 */
package chatSystemController;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import chatSystemIHMs.ChatGUI;
import chatSystemIHMs.TransferException;
import chatSystemModel.*;
import chatSystemNetwork.ChatNI;

/**
 * @author joanna
 * The controller is the only one who can update the different models.Le contrôleur est le seul à ecrire dans le modèle, c'est lui qui le met a jour
 * It directly controls the chatNI 
 * Il ne fait pas appel au ChatGUI, c'est au niveau des "update" que les envois se declenchent.
 * Il contrôle directement le chatNI
 */
public class Controller extends Thread{

	private ModelListUsers modelListUsers;
	private ModelStates modelStates;	
	private ModelText modelText;
	private ModelUsername modelUsername;
	private ChatGUI chatgui;
	private ChatNI chatNI;
	private int numFileMax;
	private int maxWrite;
	private int maxRead;
	private HashMap <Integer,ModelFileToSend> filesToSend;
	private HashMap <Integer,ModelFileToReceive> filesToReceive;
	private HashMap <String,Integer> fileToDemand;
	private long maxSizeToTransfer;
	private long nbSendingBytes;
	private long nbReceivingBytes;
	private int currentidSendDemand;
	private int maxidDemand;
	private ArrayBlockingQueue <ModelFileToSend> ftoSend;
	
	/**
	 * Controller Constructor
	 * @param modelListUsers
	 * @param modelStates
	 * @param modelText
	 * @param modelUsername
	 */
	public Controller(ModelListUsers modelListUsers,
			ModelStates modelStates,
			ModelText modelText,
			ModelUsername modelUsername) {
		this.modelListUsers = modelListUsers;
		this.modelStates = modelStates;
		this.modelText = modelText;
		this.modelUsername = modelUsername;
		this.numFileMax = 5;
		this.ftoSend = new ArrayBlockingQueue<ModelFileToSend>(this.numFileMax,true);
		this.maxRead = 1024;
		this.maxWrite = 1024;
		this.currentidSendDemand = 0;
		this.maxidDemand = 50; // on suppose que quelqu'un n'enverra pas  plus 50 fichiers dans une seule session
		this.filesToSend = new HashMap <Integer,ModelFileToSend> (); // key = idDemand 
		this.filesToReceive = new HashMap<Integer,ModelFileToReceive> (); //key = idDemand
		this.fileToDemand = new HashMap <String,Integer>();//key = name of file, value = id demand 
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
	
	/**
	 * Handle local user connection
	 * @param username
	 */
	public void performConnect(String username){
		// enregistrement du pseudo
		this.modelUsername.setUsername(username);
		// passage dans l'etat connecte
		this.modelStates.setState(true);
		// lancement de la connexion
		this.chatNI.connect(false);
		// on lance le thread du controller pour les fichiers
		this.start();	
	}
	
	/**
	 * Handle local user disconnection
	 */
	public void performDisconnect(){
		if (this.modelStates.isConnected()){
			// on passe l'etat a deconnecte
			this.modelStates.setState(false);
			// on reinitialise la liste des utilisateurs distants
			this.modelListUsers.clearListUsers();
			// on lance la deconnexion
			this.chatNI.disconnect();
		}
	}
	
	/**
	 * Handle local user's text sending
	 * @param remote message recipient
	 * @param text text to send
	 */
	public void performSendText (String remote,String text){
		InetAddress ipRecipient;
		ipRecipient=this.modelListUsers.getListUsers().get(remote);
		this.chatNI.sendMsgText(ipRecipient, text);
	}
	
	/**
	 * Launch file sending
	 * @param f 
	 */
	public void performJoinFile(ModelFileToSend f){		
		this.ftoSend.add(f);
	}
	
	/**
	 * Handle file propositions from local user
	 * @param remote user proposition recipient
	 * @param filePath file path in local disk
	 */
	public void performPropositionFile(String remote, String filePath) throws TransferException{
		// on cree le modelFile
		ModelFileToSend f = new ModelFileToSend (remote,filePath,this.currentidSendDemand,this.maxRead);
		this.nbSendingBytes += f.getSize();
		if ((this.nbSendingBytes<=this.maxSizeToTransfer) && (this.ftoSend.size() < this.numFileMax)){
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
		}else if(this.nbSendingBytes > this.maxSizeToTransfer){
			throw new TransferException(1);
		}else if (this.ftoSend.size() == this.numFileMax){
			throw new TransferException(2);
		}
	}

	/**
	 * Handle local user's answers to file proposition
	 * @param file name of file
	 * @param answer local user's answer
	 */
	public void performFileAnswer(String remote,String file, boolean answer) throws TransferException{
		
		ModelFileToReceive f = this.filesToReceive.get(this.fileToDemand.get(file));
		InetAddress ipRemote = this.modelListUsers.getListUsers().get(remote);
		if (answer == true){
			if (f.getExist()){
				f.cleanFile();
			}
			// si l'utilisateur veut accepter plus que le maximum autorise on ne reçoit pas le fichier
			if (this.nbReceivingBytes + f.getSize() > this.maxSizeToTransfer){
				this.chatNI.sendConfirmationFile(remote,ipRemote, f.getName(), false, f.getIdDemand());
				f.deleteFile();
				f.deleteObservers();
				this.filesToReceive.remove(this.fileToDemand.get(file));
				throw new TransferException(1);
			}else{
				this.nbReceivingBytes += f.getSize();
				this.chatNI.sendConfirmationFile(remote,ipRemote, f.getName(), answer, f.getIdDemand());
			}
		}else{
			this.chatNI.sendConfirmationFile(remote,ipRemote, f.getName(), answer, f.getIdDemand());
			if(!f.getExist()){
				f.deleteFile();
			}
			f.deleteObservers();
			this.filesToReceive.remove(this.fileToDemand.get(file));
		}
	}
	
	/**
	 * Handle file transfer demands from other users
	 * @param remote recipient
	 * @param file file name
	 * @param size file size
	 * @param idDemand
	 */
	public void filePropositionReceived(String remote, String file, long size, int idDemand){
		if (this.modelStates.isConnected() && this.modelListUsers.isInListUsers(remote)){
			ModelFileToReceive f = new ModelFileToReceive(remote,file,size,idDemand,this.maxWrite);
			f.addObserver(chatgui);
			System.out.println("Controller --> idDemand received : " + idDemand);
			System.out.println("Controller --> file demand received from " + remote);
			this.filesToReceive.put(idDemand, f);
			this.fileToDemand.put(file, idDemand);
			f.setStateReceivedDemand(true);
		}
	}
	
	/**
	 * Handle file tranfer answer from local user
	 * @param remote user who answers
	 * @param idDemand
	 * @param isAccepted answer
	 */
	public void fileAnswerReceived(String remote,int idDemand,boolean isAccepted){
		if (this.modelStates.isConnected() && this.modelListUsers.isInListUsers(remote)){
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
	 * Handle the end of receive transfers
	 * @param idDemand
	 */
	public void fileReceived(int idDemand){
		this.filesToReceive.remove(idDemand);
	}
	
	/**
	 * Handle textual messages from remote users 
	 * @param text
	 * @param username remote user
	 */
	public void messageReceived(String text, String username){
		if (modelStates.isConnected() && this.modelListUsers.isInListUsers(username)){
			this.modelText.setRemote(username);
			this.modelText.setTextReceived(text);
		}
	}
	
	/**
	 * Handle remote users' connection
	 * @param username remote user who connects
	 * @param ipRemote user's IP address 
	 * @param isAck inform about if we need to answer or not 
	 */
	public void connectReceived(String username,InetAddress ipRemote, boolean isAck){		
		if (modelStates.isConnected()){
			// si ack = false c'est une demande de connexion donc on repond	
			if (!isAck){
				this.chatNI.connect(true);
			}
			if ((!this.modelListUsers.isInListUsers(username)) ){					
				this.modelListUsers.addUsernameList(username, ipRemote);
			}	
		}
	}
	
	/**
	 * handle remote users' disconnection
	 * @param username remote user
	 */
	public void disconnectReceived(String username){
		if (this.modelStates.isConnected() && this.modelListUsers.isInListUsers(username)){
			this.modelListUsers.removeUsernameList(username);
			System.out.println(username + " s'est deconnecté");	
		}			
	}
	
	/**
	 * Handle file part receiving 
	 * @param fileBytes file part to write in ModelFileToReceive
	 * @param idDemand
	 * @param isLast indicates of it is the last part to receive or not
	 */
	public void partReceived(byte[] fileBytes,int idDemand, boolean isLast){	
		this.filesToReceive.get(idDemand).writeFilePart(fileBytes, isLast);
	}
	
	/**
	 * Handle the sending of files
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