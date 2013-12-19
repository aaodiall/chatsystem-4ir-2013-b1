package chatSystemNetwork;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import chatSystemModel.ModelStates;
import chatSystemModel.ModelUsername;
import chatSystemCommon.FilePart;
import chatSystemController.Controller;

/**
 * this class handles all the network classes
 * @author joanna
 *
 */
public class ChatNI implements Runnable,Observer,FromRemoteApp,ToRemoteApp{	
	
	private Controller controller;
	private ChatNIDatagramSender chatNIMessage;
	private ChatNIDatagramReceiver chatNIDatagramReceiver;
	private DatagramSocket socketUDP;
	private InetAddress userIP;
	private InetAddress userIPBroadcast;
	private NICache cache;
	private boolean connected;
	private Thread chatNIThread;
	private int portUDP;
	

	/**
	 * this function creates a new ChatNI
	 * @param portUDP port UDP pour l'envoi et la recception des messages
	 * @param numMsgMax nombre de messages qu'on peut stocker au maximum
	 * @param controller controller du NI
	 */
	public ChatNI(int portUDP,int numMsgMax, Controller controller) throws SocketException{
		this.controller=controller;
		this.portUDP = portUDP;
		this.connected = true;
	}
	
	private void initialization(int portUDP){
		this.cache = new NICache();
		try{
			this.setlocalIPandBroadcast();
		}catch(NetworkException e){
			e.displayException();
			System.exit(-1);
		}		
		this.setUDPsocket(portUDP);
	}
	
	/**
	 * creates an UDP socket bind with port  
	 * @param port port UDP de reception
	 */
	public void setUDPsocket(int port){
		try {
			this.socketUDP = new DatagramSocket(port);
		}catch(SocketException e){
			System.err.println("Error : a chat system process is already running");
			System.err.println("Please close this process and restart the application");
			System.exit(-1);
		}
	}
	
	/**
	 * find the local IP address and the local IP broadcast address
	 */
	public void setlocalIPandBroadcast()throws NetworkException{
		NetworkInterface ni;
		Iterator <InterfaceAddress> ipAddrEnum;
		InterfaceAddress intAddr;
		boolean trouve = false;
		try{
			Enumeration <NetworkInterface> localInterfaces = NetworkInterface.getNetworkInterfaces();
			while (localInterfaces.hasMoreElements() && !trouve){
				ni = localInterfaces.nextElement();
				if (ni.isUp() && !ni.isLoopback()){
					ipAddrEnum = ni.getInterfaceAddresses().iterator();
					while (ipAddrEnum.hasNext() && !trouve){
						intAddr = ipAddrEnum.next();
						if (intAddr.getBroadcast() != null){
							this.userIP = intAddr.getAddress();
							this.userIPBroadcast = intAddr.getBroadcast();
							this.cache.setBroadcast(userIPBroadcast);
							trouve = true;
							System.out.println("local IP : " + this.userIP.toString());
							System.out.println("local Broadcast : " + this.userIPBroadcast.toString());									
						}
					}
				}
			}
			if(!trouve){
				throw new NetworkException("no IP address found");
			}
		}catch (SocketException e){
			System.err.println("Error : no network interface found");
			System.err.println("Please check your Internet connection and restart the application");
			System.exit(-1);
		}
	}	
	
	
	@Override
	public void connect(boolean ack){
		if (ack == false){
			// on lance un récepteur de packet UDP
			this.chatNIDatagramReceiver = new ChatNIDatagramReceiver(this,this.socketUDP,this.userIP);
			this.chatNIMessage = new ChatNIDatagramSender(this.socketUDP);
			this.chatNIDatagramReceiver.start();
			// on lance un émetteur de packet UDP
			this.chatNIMessage.start();		
			this.chatNIThread = new Thread(this);
			this.chatNIThread.start();
		}
		this.chatNIMessage.sendHello(this.cache.getHello(ack), this.cache.getBroadcast());
	}	
	
	
	@Override
	public void disconnect(){
		this.chatNIMessage.sendBye(this.cache.getUsername(), this.cache.getBroadcast());
		// on ferme le récepteur de packet UDP et on nettoie le cache avant de fermer
		this.cache.clear();
		this.cache = null;
	}	
	

	@Override
	public void sendMsgText(InetAddress recipient, String text2Send){
		this.chatNIMessage.sendText(recipient, text2Send, this.cache.getUsername());
	}	
	

	@Override
	public void sendPropositionFile(String remote, InetAddress recipient, String fileName, long size, int idDemand){
		// on cree un ChatNIStreamSender pour envoyer le fichier
		ChatNIStreamSender sender = new ChatNIStreamSender();
		this.cache.addSender(idDemand,sender);
		this.chatNIMessage.sendFileTransfertDemand(this.cache.getUsername(),recipient,fileName,size,sender.getNumPort(),idDemand);	
	}
	
	@Override
	public void sendConfirmationFile(String recipientName, InetAddress recipientIP, String fileName,boolean answer, int idDemand){
		// si l'utilisateur veut recevoir le fichier
		this.chatNIMessage.sendFileTransfertConfirmation(this.cache.getUsername(),recipientIP, answer, idDemand);
		if (answer == true){
			ChatNIStreamReceiver sr = new ChatNIStreamReceiver(this,this.cache.getRemotePort(idDemand),recipientIP,idDemand);
			sr.start();
		}else{
			this.cache.removeRemotePort(idDemand);
		}
	}
	
	@Override
	public void sendPart(byte[] part, int idDemand, boolean isLast){
		FilePart f = new FilePart(this.cache.getUsername(),part,isLast);
		this.cache.getSender(idDemand).sendPart(f);
		// suppression au niveau du cache des informations sur le fichier envoye si c'est la dernière partie 
		if (isLast){
			this.cache.removeSender(idDemand);
		}
	}
	
	@Override
	public void helloReceived(String remoteUsername, InetAddress remoteIP, boolean isAck){
		this.controller.connectReceived(remoteUsername, remoteIP, isAck);
	}
	
	@Override
	public void goodbyeReceived(String remoteUsername){
		this.controller.disconnectReceived(remoteUsername);
	}
	
	@Override
	public void textReceived(String remoteUsername, String text){
		this.controller.messageReceived(text, remoteUsername);
	}
	
	@Override
	public void fileTansfertDemandReceived(String remoteUsername, String fileName, long fileSize, int idDemand, int remotePort){
		// on enregistre les infos dudans le cache pour
		this.cache.addRemotePort(idDemand,remotePort);
		this.controller.filePropositionReceived(remoteUsername,fileName,fileSize,idDemand);
	}
	
	@Override
	public void fileTansfertConfirmationReceived(String remoteUsername,int idDemand, boolean isAccepted){
		/*	si la demande est acceptée on lance le Stream sender asscoié
		 *  sinon on supprime le Stream sender associé
		 *  dans tous les cas on fait suivre l'information au controller
		 */
		if (isAccepted){
			this.cache.getSender(idDemand).start();
		}else{
			this.cache.removeSender(idDemand);
		}
		this.controller.fileAnswerReceived(remoteUsername,idDemand,isAccepted);
	}
	
	@Override
	public synchronized void filePartReceived(byte[] fileBytes, int idDemand, boolean isLast){
		// on fais directement suivre la partie au controller
		this.controller.partReceived(fileBytes, idDemand, isLast);
		if (isLast){
			// suppression, au niveau du cache, des informations sur le fichier reçu
			this.cache.removeRemotePort(idDemand);
		}
	}	
	
	/**
	 * launch the datagram receiver and then check sends Hello periodically.
	 * if the local user disconnects it stops the datagram receiver and cleans the NICache 
	 */
	@Override
	public void run(){
		/*	tant que l'utilisateur est connecté le thread relance une connexion toutes les 2 minutes
			il peut ainsi verifier les connections et signaler aux autres utilisateurs que l'utilisateur est toujours connecté */ 
		while(this.connected){
			try{
				Thread.sleep(60000);
				this.chatNIMessage.sendHello(this.cache.getHello(false), this.cache.getBroadcast());
			} catch (InterruptedException e){}
		}
	}
	
	/**
	 * the method is used when the chatNI is notified of an event (by its observable objects)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// si il y a eu un changement de username
		if(arg0 instanceof ModelUsername){
			this.initialization(this.portUDP);
			this.cache.setUsername((String)arg1);
		}
		// si il y a eu un setStateConnected
		if(arg0 instanceof ModelStates){
			// utilisateur est deconnecte
			if (((Boolean)arg1).equals(false)){
				this.connected = false;
				if(this.chatNIThread.getState() == Thread.State.TIMED_WAITING){
					this.chatNIThread.interrupt();
				}
				this.chatNIMessage.setConnected(false);
				this.chatNIDatagramReceiver.setConnected(false);
			}
		}
	}
}