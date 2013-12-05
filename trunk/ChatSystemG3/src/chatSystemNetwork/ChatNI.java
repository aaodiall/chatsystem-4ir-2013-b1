/**
 * 
 */
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
import chatSystemIHMs.View;
import chatSystemModel.ModelStates;
import chatSystemModel.ModelUsername;
import chatSystemCommon.FilePart;
import chatSystemController.Controller;

/* Note pour plus tard : 
 *  - ecrire la javadoc et les commentaires
 */


public class ChatNI extends View implements Runnable,Observer,FromRemoteApp,ToRemoteApp{	
	
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
	 * 
	 * @param portUDP port UDP pour l'envoi et la recception des messages
	 * @param numMsgMax nombre de messages qu'on peut stocker au maximum
	 * @param controller controller du NI
	 */
	public ChatNI(int portUDP,int numMsgMax, Controller controller){
		this.controller=controller;
		this.cache = new NICache();
		this.setlocalIPandBroadcast();
		this.setUDPsocket(portUDP, numMsgMax);
		this.chatNIMessage = new ChatNIDatagramSender(numMsgMax,this.socketUDP);
		this.chatNIMessage.start();
		this.numMsgMax = numMsgMax;
		this.connected = true;
		this.chatNIThread = new Thread(this);
		this.chatNIThread.start();
	}
	
	/**
	 * 
	 * @param port port UDP de reception
	 * @param numMsgMax nombre de pdu UDP qu'on peut stocker au maximum
	 */
	public void setUDPsocket(int port, int numMsgMax){
		try {
			this.socketUDP = new DatagramSocket(port);
		}catch(SocketException sockExc){
			if (this.socketUDP == null)
				System.out.println("socketUDP : socket exception");
			sockExc.printStackTrace();
		}
	}
	
	public void setlocalIPandBroadcast(){
		Enumeration <NetworkInterface> localInterfaces;
		NetworkInterface ni;
		Iterator <InterfaceAddress> ipAddrEnum;
		InterfaceAddress intAddr;
		boolean trouve = false;
		try{
			localInterfaces = NetworkInterface.getNetworkInterfaces();
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
			System.out.println("error : socket exception ip adresses");
			e.printStackTrace();
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
		ChatNIStreamSender sender = new ChatNIStreamSender(this,idDemand);
		System.out.println("port server  "+sender.getNumPort());
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
			this.cache.removeRemoteUsername(idDemand);
		}
	}
	
	public void sendPart(byte[] part, int idDemand, boolean isLast){
		FilePart f = new FilePart(this.cache.getUsername(),part,isLast);
		this.cache.getSender(idDemand).sendPart(f);
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
		System.out.println("ChatNI -->"+ remoteUsername + " demand  "+ idDemand +" port  "+remotePort);
		this.cache.addRemotePort(idDemand,remotePort);
		this.cache.addRemoteUsername(remoteUsername, idDemand);
		this.controller.filePropositionReceived(remoteUsername,fileName,fileSize,idDemand);
	}
	
	public void fileTansfertConfirmationReceived(String remoteUsername,int idDemand, boolean isAccepted){
		if (isAccepted){
			// on lance le thread
			this.cache.getSender(idDemand).start();
		}else{
			this.cache.removeSender(idDemand);//this.cache.getLocalDemand(idDemand));
			//this.cache.removeLocalDemand(idDemand);
		}
		this.controller.fileAnswerReceived(remoteUsername,idDemand,isAccepted);
	}
	
	public void fileTansfertCancelReceived(String remoteUsername,int idDemand){
		this.controller.fileTranfertCancelReceived(remoteUsername,idDemand);
	}
	
	public void filePartReceived(byte[] fileBytes, int idDemand, boolean isLast){
		this.controller.partReceived(fileBytes, idDemand, isLast);
	}	
	
	public void fileSent(int idDemand){
		// suppression au niveau du cache des informations sur le fichier envoye 
		this.cache.removeSender(idDemand);
	}
	
	public void fileReceived(int idDemand){
		// suppression, au niveau du cache, des informations sur le fichier reçu
		this.cache.removeRemotePort(idDemand);
		this.cache.removeRemoteUsername(idDemand);
		// notification du "controller"
		this.controller.fileReceived(idDemand);
	}
	
	public void run(){
		ChatNIDatagramReceiver chatNIDatagramReceiver = new ChatNIDatagramReceiver(this,this.numMsgMax,this.socketUDP,this.userIP);
		chatNIDatagramReceiver.start();
		while(this.connected){
			try{
				Thread.sleep(120000);
				this.connect(false);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		chatNIDatagramReceiver.setConnected(false);
		this.cache.clear();
		this.cache = null;
	}
	
	
	public void update(Observable arg0, Object arg1) {
		// si il y a eu un changement de username
		if(arg0.getClass() == ModelUsername.class){
			this.cache.setUsername((String)arg1);
		}
		// si il y a eu un setStateConnected
		if(arg0.getClass().equals(ModelStates.class)){
			// utilisateur est deconnecte
			if (arg1.equals(false)){
				this.connected = false;
				this.chatNIMessage.setConnected(false);
			}
		}
	}

}