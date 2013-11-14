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

import runChat.ChatSystem;
import chatSystemIHMs.View;
import chatSystemModel.ModelStates;
import chatSystemCommon.*;
import chatSystemController.Controller;

/* Note pour plus tard : 
 *  - tester le programme sur un reseau local
 *  - decider si on utilise la liste du model ou une liste de chatNI qui sera actualisee a chaque changement
 *  - definir le port udp comme il faut
 *  - decider si on utilise a chaque fois un seul objet hello,bye,text juste en modifiant les attributs
 *  - ecrire la javadoc et les commentaires
 */


public class ChatNI extends View implements Runnable, Observer{

	/* ATTRIBUTS A METTRE
	 * final int portUDP;
	 * Controller controller;
	 * ChatNIMessage chatNIMessage;
	 * ChatNIStreamConnexion chatNIStreamConnexion;
	 * DatagramSocket socketUDP
	 * InetAddress userIP
	 * InetAddress userIPBroadcast
	 * ArrayList<DatagramPacket> bufferPDUReceived
	 * byte[] streamReceived;
	 * DatagramPacket pduReceived;
	 * 
	 * METHODES A IMPLEMENTER
	 * public ChatNI(int portUDP, Controller controller, ChatNIMessage chatNIMessage, ChatNIStreamConnexion chatNIStreamConnexion);
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
	private Controller controller;
	private ChatNIMessage chatNIMessage; 
	//private ChatNIStreamConnexion chatNIStreamConnexion;
	private DatagramSocket socketUDP;
	private InetAddress userIP;
	private InetAddress userIPBroadcast;
	private ArrayList<DatagramPacket> bufferPDUReceived;
	private byte[] streamReceived;
	private DatagramPacket pduReceived;


	public ChatNI(int portUDP, Controller controller,int bufferSize){
		// associe son controlleur
		this.controller=controller;
		//crée son chatNIStreamConnexion
		//this.chatNIStreamConnexion=new ChatNIStreamConnexion();
		//initialisation du buffer de reception
		bufferPDUReceived = new ArrayList<DatagramPacket>(bufferSize) ;
		// initialisation du port UDP de ChatNI
		this.portUDP = portUDP;
		// recuperation de l'IPUser et de l'IPBroadcast
		this.setlocalIPandBroadcast();
		//System.out.println("local IP : " + this.userIP.toString());
		//System.out.println("local Broadcast : " + this.userIPBroadcast.toString());
		try {
			// construction du socket UDP
			this.socketUDP = new DatagramSocket(this.portUDP,this.userIP);
			// initialisation d'un pdu de reception
			this.streamReceived = new byte[this.socketUDP.getReceiveBufferSize()];
			this.pduReceived = new DatagramPacket(this.streamReceived,this.streamReceived.length);
			//cree son chatNIMessage
			//this.chatNIMessage=new ChatNIMessage(this.socketUDP, bufferSize, ChatSystem.getModelListUsers(), ChatSystem.getModelUsername(),this.userIP, this.userIPBroadcast);
			//crée son chatNIMessage
			this.chatNIMessage=new ChatNIMessage(this.socketUDP, this.socketUDP.getReceiveBufferSize(), ChatSystem.getModelListUsers(), ChatSystem.getModelUsername(),this.userIP, this.userIPBroadcast);
		}catch(SocketException sockExc){
			if (this.socketUDP == null)
				System.out.println("socketUDP : socket exception");
			sockExc.printStackTrace();
			this.socketUDP.close();
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
				//version pour Tests locaux
				if (ni.isLoopback()){
					ipAddrEnum = ni.getInterfaceAddresses().iterator();
					while (ipAddrEnum.hasNext() && !trouve){
						intAddr = ipAddrEnum.next();
						if (((InterfaceAddress)intAddr).getAddress().getClass() == Inet4Address.class){
							//try{
								this.userIP = ((InterfaceAddress)intAddr).getAddress();//InetAddress.getLocalHost();
							//}catch (UnknownHostException e){
								//System.out.println("no host");
							//}
							this.userIPBroadcast = ((InterfaceAddress)intAddr).getAddress();
							System.out.println("local IP : " + this.userIP.toString());
							System.out.println("local Broadcast : " + this.userIPBroadcast.toString());									
						}
					}
				} // FIN VERSION pour TESTS LOCAUX
				/*if (ni.isUp() && !ni.isLoopback()){
					ipAddrEnum = ni.getInterfaceAddresses().iterator();
					while (ipAddrEnum.hasNext() && !trouve){
						intAddr = ipAddrEnum.next();
						if (((InterfaceAddress)intAddr).getBroadcast() != null){
							this.userIP = ((InterfaceAddress)intAddr).getAddress();
							this.userIPBroadcast = ((InterfaceAddress)intAddr).getBroadcast();
							//System.out.println("local IP : " + this.userIP.toString());
							//System.out.println("local Broadcast : " + this.userIPBroadcast.toString());									
						}
					}
				}*/
			}
		}catch (SocketException iPAddresses){
			System.out.println("error : socket exception ip adresses");
		}
	}
	
	public void connect(String username, boolean ack){
		this.chatNIMessage.sendHello(username, ack);
	}
	
	
	public void disconnect(String username){
		this.chatNIMessage.sendBye(username);
	}
	
	
	public void sendMsgText(ArrayList<String> usernameList, String text2Send,String username){
		this.chatNIMessage.sendText(usernameList, text2Send,username);
	}
	
	public void sendMsgFile(String recipient_username, String fileName){
		this.chatNIMessage.sendFileTransfertDemand(recipient_username, fileName);
	}
	
	public void pduAnalyze(){
		int first = 0;
		Message receivedMsg;
		InetAddress ipRemoteAddr;
		ipRemoteAddr = this.bufferPDUReceived.get(first).getAddress();
		try {
			receivedMsg = Message.fromArray(this.bufferPDUReceived.get(first).getData());
			// si c'est un hello on fait le signale au controller
			if (receivedMsg.getClass() == Hello.class){
				controller.connectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr), ipRemoteAddr,((Hello)receivedMsg).isAck());
			}else if(receivedMsg.getClass() == Text.class){
				controller.messageReceived(((Text)receivedMsg).getText(), this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
			}else if(receivedMsg.getClass() == Goodbye.class){
				controller.disconnectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
			}
			this.bufferPDUReceived.remove(first);
		}catch (IOException recExc){
			System.out.println("error : fromArray receive");
		}			
	}
	
	public void run(){
		// on se met en attente de reception d'un pdu
		try {
			System.out.println("attend pdu");
			this.socketUDP.receive(this.pduReceived);
			System.out.println("fin attend pdu");
			// on a recu un pdu donc on traite le message qu'il contient
			this.bufferPDUReceived.add(pduReceived);
			this.pduAnalyze();
		}catch (IOException sockRec){
			System.out.println("error receive socket");
		}			
	}
	
	public String makeUsername(String username, InetAddress ip){		
		return username +"@"+ ip.getHostAddress();
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
		if(arg0.getClass().equals(ModelStates.class)){
			
		}
	}

}
