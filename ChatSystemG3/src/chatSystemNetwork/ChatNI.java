/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;

import runChat.ChatSystem;
import chatSystemIHMs.View;
import chatSystemModel.ModelStates;
import chatSystemModel.ModelUsername;
import chatSystemCommon.*;
import chatSystemController.Controller;

/* Note pour plus tard : 
 *  - decider si on utilise a chaque fois un seul objet hello,bye,text juste en modifiant les attributs
 *  - ecrire la javadoc et les commentaires
 */


public class ChatNI extends View implements Runnable, Observer{

	/* ATTRIBUTS A METTRE
	 * final int portUDP;
	 * Controller controller;
	 * ChatNIMessage chatNIMessage;
	 * ChatNIStreamConnection chatNIStreamConnection;
	 * DatagramSocket socketUDP
	 * InetAddress userIP
	 * InetAddress userIPBroadcast
	 * ArrayList<DatagramPacket> bufferPDUReceived
	 * byte[] streamReceived;
	 * DatagramPacket pduReceived;
	 * 
	 * METHODES A IMPLEMENTER
	 * public ChatNI(int portUDP, Controller controller, ChatNIMessage chatNIMessage, ChatNIStreamConnection chatNIStreamConnection);
	 * public void setlocalIPandBroadcast();
	 * public void connect(String username, boolean ack);
	 * public void disconnect();
	 * public void sendMsgText(ArrayList<String> usernameList, String text2Send);
	 * public void sendMsgFile(String recipient_username, String file_name);
	 * public String make_username(String name, InetAddress ip);
	 * public void pduAnalyze();
	 * public void run();
	*/
	
	
	private final int portUDP;
	private int portTCPServer;
	private int bufferSize;
	private Controller controller;
	private ChatNIMessage chatNIMessage; 
	private ChatNIStreamConnection chatNIStreamConnection;
	private DatagramSocket socketUDP;
	private InetAddress userIP;
	private InetAddress userIPBroadcast;
	private ArrayBlockingQueue <DatagramPacket> bufferPDUReceived;
	private byte[] streamReceived;
	private DatagramPacket pduReceived;
	private Thread chatNIMessageThread;
	private Thread chatNIStreamConnectionThread;


	public ChatNI(int portUDP,int bufferSize, Controller controller){
		// associe son controlleur
		this.controller=controller;
		// initialisation du port UDP de ChatNI
		this.portUDP = portUDP;
		// initialisation du portTCPServer
		this.portTCPServer = 8000;
		//cree son chatNIStreamConnection
		//this.chatNIStreamConnection = new ChatNIStreamConnection(this.portTCPServer);
		// recuperation de l'IPUser et de l'IPBroadcast
		this.setlocalIPandBroadcast();
		// on dimensionne le buffer de reception
		this.bufferSize = bufferSize;
		try {
			// construction du socket UDP
			this.socketUDP = new DatagramSocket(this.portUDP);
			//initialisation du buffer de reception
			this.bufferPDUReceived = new ArrayBlockingQueue<DatagramPacket>(this.bufferSize) ;
			// initialisation d'un pdu de reception
			this.streamReceived = new byte[this.socketUDP.getReceiveBufferSize()];
			this.pduReceived = new DatagramPacket(this.streamReceived,this.streamReceived.length);
		}catch(SocketException sockExc){
			if (this.socketUDP == null)
				System.out.println("socketUDP : socket exception");
			sockExc.printStackTrace();
			this.socketUDP.close();
		}
		//cree son chatNIMessage
		this.chatNIMessage = new ChatNIMessage(this.bufferSize,this.socketUDP, ChatSystem.getModelListUsers(), this.userIPBroadcast);
		// cree son chatNIStreamConnection
		this.chatNIStreamConnection = new ChatNIStreamConnection(this.portTCPServer);
		//lance le thread de chatNIMessage
		this.chatNIMessageThread = new Thread(this.chatNIMessage);
		this.chatNIMessageThread.start();
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
	
	public void connect(String username, boolean ack){
		this.chatNIMessage.sendHello(username, ack);
	}
	
	
	public void disconnect(String username){
		this.chatNIMessage.sendBye(username);
	}
	
	
	public void sendMsgText(InetAddress recipient, String text2Send, String username){
		this.chatNIMessage.sendText(recipient, text2Send, username);
	}
	
	public void sendPropositionFile(String recipientUsername, String fileName, long size){
		// on lance l'envoi de la demande
		this.chatNIMessage.sendFileTransfertDemand(recipientUsername, fileName, size,this.portTCPServer);
		// on crée le socket 
		this.portTCPServer++;
	}
	
	public void sendMsgFile(){
		// on lance le Thread de ChatNIStreamConnection
		//Thread chatNIStreamConnectionThread = new Thread(this.chatNIStreamConnection);
		//chatNIStreamConnectionThread.start();
	}
	
	public void pduAnalyze(){
		Message receivedMsg;
		InetAddress ipRemoteAddr;
		ipRemoteAddr = this.bufferPDUReceived.peek().getAddress();
		DatagramPacket pdureceived;
		if (!this.userIP.equals(ipRemoteAddr)){
			try {
				pdureceived = this.bufferPDUReceived.poll();
				receivedMsg = Message.fromArray(pdureceived.getData());
				// si c'est un hello on fait le signale au controller
				if (receivedMsg.getClass() == Hello.class){
					controller.connectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr), ipRemoteAddr,((Hello)receivedMsg).isAck());
				}else if(receivedMsg.getClass() == Text.class){
					controller.messageReceived(((Text)receivedMsg).getText(), this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
				}else if(receivedMsg.getClass() == Goodbye.class){
					controller.disconnectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
				}				
			}catch (IOException recExc){
				System.out.println("error : cannot transform PDUdata in Message");
				recExc.printStackTrace();
			}
		}
	}
	
	public void run(){
		while(true){
			// on se met en attente de reception d'un pdu
			try {
				Thread.sleep(40);
				System.out.println("attend pdu");
				this.socketUDP.receive(this.pduReceived);
				System.out.println("fin attend pdu");
				// on a recu un pdu donc on traite le message qu'il contient
				this.bufferPDUReceived.add(pduReceived);
				this.pduAnalyze();
			}catch (InterruptedException e) {
				System.out.println("error : sleep interrupted in R-Thread");	
				e.printStackTrace();
			}catch (IOException sockRec){
				System.out.println("error : receive socket");	
				sockRec.printStackTrace();
			}
		}
	}
	
	public String makeUsername(String username, InetAddress ip){		
		return username +"@"+ ip.getHostAddress();
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		/*//s'il y a eu un setUsername
		if(arg0.getClass().equals(ModelUsername.class)){
			this.connect(((String)arg1), false);
		}else
		// si il y a eu un setStateConnected
		if(arg0.getClass().equals(ModelStates.class)){
			// utilisateur deconnecte
			if (arg1.equals(false)){
				this.disconnect(ChatSystem.getModelUsername().getUsername());
			}
		}*/
	}

}