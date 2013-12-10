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
	private DatagramSocket socketUDP;
	private InetAddress userIP;
	private InetAddress userIPBroadcast;
	private NICache cache;
	private boolean connected;
	private Thread chatNIThread;
	private int numMsgMax;
	

	/**
	 * this function creates a new ChatNI
	 * @param portUDP port UDP pour l'envoi et la recception des messages
	 * @param numMsgMax nombre de messages qu'on peut stocker au maximum
	 * @param controller controller du NI
	 */
	public ChatNI(int portUDP,int numMsgMax, Controller controller){
		this.controller=controller;
		this.cache = new NICache();
		this.setlocalIPandBroadcast();
		this.setUDPsocket(portUDP);
		this.chatNIMessage = new ChatNIDatagramSender(numMsgMax,this.socketUDP);
		this.chatNIMessage.start();
		this.numMsgMax = numMsgMax;
		this.connected = true;
		this.chatNIThread = new Thread(this);
		this.chatNIThread.start();
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
	public void setlocalIPandBroadcast(){
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
						if (((InterfaceAddress)intAddr).getBroadcast() != null){
							this.userIP = ((InterfaceAddress)intAddr).getAddress();
							this.userIPBroadcast = ((InterfaceAddress)intAddr).getBroadcast();
							this.cache.setBroadcast(userIPBroadcast);
							System.out.println("local IP : " + this.userIP.toString());
							System.out.println("local Broadcast : " + this.userIPBroadcast.toString());									
						}
					}
				}
			}
		}catch (SocketException e){
			System.err.println("Error : no network interface found");
			System.err.println("Please check your Internet connection and restart the application");
			System.exit(-1);
		}
	}	
	
	
	public void connect(boolean ack){
		this.chatNIMessage.sendHello(this.cache.getHello(ack), this.cache.getBroadcast());
	}	
	
	
	public void disconnect(){
		this.chatNIMessage.sendBye(this.cache.getUsername(), this.cache.getBroadcast());
	}	
	

	public void sendMsgText(InetAddress recipient, String text2Send){
		this.chatNIMessage.sendText(recipient, text2Send, this.cache.getUsername());
	}	
	

	public void sendPropositionFile(String remote, InetAddress recipient, String fileName, long size, int idDemand){
		// on cree un ChatNIStreamSender pour envoyer le fichier
		ChatNIStreamSender sender = new ChatNIStreamSender();
		this.cache.addSender(idDemand,sender);
		this.chatNIMessage.sendFileTransfertDemand(this.cache.getUsername(),recipient,fileName,size,sender.getNumPort(),idDemand);	
	}
	
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
	
	public void sendPart(byte[] part, int idDemand, boolean isLast){
		FilePart f = new FilePart(this.cache.getUsername(),part,isLast);
		this.cache.getSender(idDemand).sendPart(f);
		// suppression au niveau du cache des informations sur le fichier envoye si c'est la dernière partie 
		if (isLast){
			this.cache.removeSender(idDemand);
		}
	}
	
	public void helloReceived(String remoteUsername, InetAddress remoteIP, boolean isAck){
		this.controller.connectReceived(remoteUsername, remoteIP, isAck);
	}
	
	public void goodbyeReceived(String remoteUsername){
		this.controller.disconnectReceived(remoteUsername);
	}
	
	public void textReceived(String remoteUsername, String text){
		this.controller.messageReceived(text, remoteUsername);
	}
	
	public void fileTansfertDemandReceived(String remoteUsername, String fileName, long fileSize, int idDemand, int remotePort){
		// on enregistre les infos dudans le cache pour
		System.out.println("ChatNI -->"+ remoteUsername + " demand  "+ idDemand +" port  "+remotePort);
		this.cache.addRemotePort(idDemand,remotePort);
		this.controller.filePropositionReceived(remoteUsername,fileName,fileSize,idDemand);
	}
	
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
	public void run(){
		// on lance un récepteur de packet UDP
		ChatNIDatagramReceiver chatNIDatagramReceiver = new ChatNIDatagramReceiver(this,this.numMsgMax,this.socketUDP,this.userIP);
		chatNIDatagramReceiver.start();
		/*	tant que l'utilisateur est connecté le thread relance une connexion toutes les 2 minutes
			il peut ainsi verifier les connections et signaler aux autres utilisateurs que l'utilisateur est toujours connecté */ 
		while(this.connected){
			try{
				Thread.sleep(120000);
				this.connect(false);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		// on ferme le récepteur de packet UDP et on nettoie le cache avant de fermer
		chatNIDatagramReceiver.setConnected(false);
		this.cache.clear();
		this.cache = null;
	}
	
	/**
	 * the method is used when the chatNI is notified of an event (by its observable objects)
	 */
	public void update(Observable arg0, Object arg1) {
		// si il y a eu un changement de username
		if(arg0 instanceof ModelUsername){
			this.cache.setUsername((String)arg1);
		}
		// si il y a eu un setStateConnected
		if(arg0 instanceof ModelStates){
			// utilisateur est deconnecte
			if (((Boolean)arg1).equals(false)){
				this.connected = false;
				this.chatNIMessage.setConnected(false);
			}
		}
	}
}