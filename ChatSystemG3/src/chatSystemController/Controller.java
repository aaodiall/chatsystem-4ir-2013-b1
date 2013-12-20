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
 * The controller is the only one who can update the different models.
 * It directly controls the chatNI but not the ChatGUI (it is done by update messages) 
 * @author joanna
 *
 */
public class Controller implements Runnable{

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
	private long nbReceivingBytes;
	private int currentidSendDemand;
	private int maxidDemand;
	private ArrayBlockingQueue <ModelFileToSend> ftoSend;
	
	/**
	 * Controller Constructor
	 * @param modelListUsers object of type ModelListUsers
	 * @param modelStates object of type ModelStates
	 * @param modelText object of type ModelText
	 * @param modelUsername object of type ModelUsername
	 */
	public Controller(ModelListUsers modelListUsers,
			ModelStates modelStates,
			ModelText modelText,
			ModelUsername modelUsername) {
		this.modelListUsers = modelListUsers;
		this.modelStates = modelStates;
		this.modelText = modelText;
		this.modelUsername = modelUsername;
		this.numFileMax = 1;//5;
		this.ftoSend = new ArrayBlockingQueue<ModelFileToSend>(this.numFileMax,true);
		this.maxRead = 1024;
		this.maxWrite = 1024;
		this.currentidSendDemand = 0;
		this.maxidDemand = 50; // on suppose que quelqu'un n'enverra pas  plus 50 fichiers dans une seule session
		this.filesToSend = new HashMap <Integer,ModelFileToSend> (); // key = idDemand 
		this.filesToReceive = new HashMap<Integer,ModelFileToReceive> (); //key = idDemand
		this.fileToDemand = new HashMap <String,Integer>();//key = name of file, value = id demand 
		this.maxSizeToTransfer = 760000000;//2 000 000 000;
		this.nbReceivingBytes = 0;
	}

	/**
	 * indicates to the controller the ChatGUI that it have to use 
	 * @param chatgui object of type ChatGUI
	 */
	public void setChatgui(ChatGUI chatgui) {
		this.chatgui = chatgui;
	}

	/**
	 * indicates to the controller the ChatNI that it have to use
	 * @param chatNI object of type ChatNI
	 */
	public void setChatNI(ChatNI chatNI) {
		this.chatNI = chatNI;
	}
	
	/**
	 * Handle local user connection
	 * @param username local user's user name
	 */
	public void performConnect(String username){
		// enregistrement du pseudo
		this.modelUsername.setUsername(username);
		// passage dans l'etat connecte
		this.modelStates.setState(true);
		// lancement de la connexion
		this.chatNI.connect(false);
		// on lance le thread du controller pour les fichiers
		Thread threadControl = new Thread(this);
		threadControl.start();	
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
	 * @param f representation of the file to send
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
		if ((f.getSize()<=this.maxSizeToTransfer) && (this.ftoSend.size() < this.numFileMax)){
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
		}else if(f.getSize() > this.maxSizeToTransfer){
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
		// si l'utilisateur accepte de recevoir le fichier et qu'il est dans les limites
		if (answer == true && (this.nbReceivingBytes + f.getSize() < this.maxSizeToTransfer)){
			// on regarde s'il existe deja un fichier de ce nom
			if (!f.hasBeenCreated()){
				// si oui on essaye de renommer le fichier 
				f.cleanFile();
			}
			this.nbReceivingBytes += f.getSize();
			this.chatNI.sendConfirmationFile(remote,ipRemote, f.getName(), answer, f.getIdDemand());
		// sinon on efface les informations du fichier	
		}else{
			this.chatNI.sendConfirmationFile(remote,ipRemote, f.getName(), false, f.getIdDemand());
			if (f.hasBeenCreated()){					
				f.deleteFile();
			}
			f.deleteObservers();
			this.filesToReceive.remove(this.fileToDemand.get(file));
			if (this.nbReceivingBytes + f.getSize() > this.maxSizeToTransfer){
				throw new TransferException(1);
			}
		}
	}
	
	/**
	 * Handle file transfer demands from other users
	 * @param remote recipient
	 * @param file file name
	 * @param size file size
	 * @param idDemand demand ID associated
	 */
	public void filePropositionReceived(String remote, String file, long size, int idDemand){
		if (this.modelStates.isConnected() && this.modelListUsers.isInListUsers(remote)){
			ModelFileToReceive f = new ModelFileToReceive(remote,file,size,idDemand,this.maxWrite);
			f.addObserver(chatgui);
			this.filesToReceive.put(idDemand, f);
			this.fileToDemand.put(file, idDemand);
			f.setStateReceivedDemand(true);
		}
	}
	
	/**
	 * Handle file tranfer answer from local user
	 * @param remote user who answers
	 * @param idDemand demand ID associated
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
	 * Handle textual messages from remote users 
	 * @param text received text
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
		}			
	}
	
	/**
	 * Handle file part receiving 
	 * @param fileBytes file part to write in ModelFileToReceive
	 * @param idDemand demand ID associated
	 * @param isLast indicates of it is the last part to receive or not
	 */
	public void partReceived(byte[] fileBytes,int idDemand, boolean isLast){	
		this.filesToReceive.get(idDemand).writeFilePart(fileBytes, isLast);
		if(isLast){
			this.nbReceivingBytes -=this.filesToReceive.get(idDemand).getSize();
		}
	}
	
	/**
	 * Handle the sending of files
	 */
	@Override
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