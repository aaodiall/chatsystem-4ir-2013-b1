/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import runChat.ChatSystem;
import chatSystemIHMs.View;
import chatSystemCommon.*;

/* Note pour plus tard : 
 *  - tester le programme sur un reseau local
 *  - decider si on utilise la liste du model ou une liste de chatNI qui sera actualisee a chaque changement
 *  - definir le port udp comme il faut
 *  - decider si on utilise a chaque fois un seul objet hello,bye,text juste en modifiant les attributs
 *  - ecrire la javadoc et les commentaires
 */

public class ChatNI extends View implements Runnable{
	
	private final int portUDP;
	private InetAddress localBroadcast;
	private DatagramSocket socketUDP;
	private InetAddress localUserIP;
	private ArrayList <DatagramPacket> bufferMessagesToSend;
	private DatagramPacket pduReceived;
	private byte[] streamReceived;
	private HashMap<String,InetAddress> remoteUsersList;
	
	public ChatNI(int portUDP){
		// initialisation du port UDP de ChatNI
		this.portUDP = portUDP;
		// initialisation du buffer de messages a envoyer
		this.bufferMessagesToSend = new ArrayList<DatagramPacket>();
		// initialisation de la liste des remoteUsers de ChatNI
		this.remoteUsersList = new HashMap<String,InetAddress>();
		// recuperation de l'IPUser et de l'IPBroadcast
		//this.setlocalIPandBroadcast();
		try {
			// construction du socket UDP
			this.socketUDP = new DatagramSocket(portUDP,localUserIP);
			// initialisation d'un pdu de reception
			this.streamReceived = new byte[this.socketUDP.getReceiveBufferSize()];
			this.pduReceived = new DatagramPacket(this.streamReceived,this.streamReceived.length);
		}catch(SocketException sockExc){
			if (this.socketUDP == null)
				System.out.println("socketUDP : socket exception");
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
				if (ni.isUp()){
					ipAddrEnum = ni.getInterfaceAddresses().iterator();
					while (ipAddrEnum.hasNext() && !trouve){
						intAddr = ipAddrEnum.next();
						if (((InterfaceAddress)intAddr).getBroadcast() != null){
							this.localUserIP = ((InterfaceAddress)intAddr).getAddress();
							this.localBroadcast = ((InterfaceAddress)intAddr).getBroadcast();
							System.out.println("local IP : " + this.localUserIP.toString());
							System.out.println("local Broadcast : " + this.localBroadcast.toString());									
						}
					}
				}
			}
		}catch (SocketException iPAddresses){
			System.out.println("error : socket exception ip adresses");
		}
	}
	
	public void connect(String username, boolean ack){
		byte [] helloStream;
		DatagramPacket pdu;
		// new Hello object
		Hello hello = new Hello(username,ack);
		try {
			// Objet to byte[]
			helloStream = ((Message)hello).toArray();
			// Enable Broadcast
			this.socketUDP.setBroadcast(true);
			// make pdu
			pdu = new DatagramPacket(helloStream,helloStream.length,InetAddress.getLocalHost(),portUDP);
			// add pdu to bufferMessagesToSend
			this.bufferMessagesToSend.add(pdu);
			// send pdu
			this.run();
			this.socketUDP.setBroadcast(false);			
		}catch (IOException e){
			System.out.println("connection failed");
		}
	}
	
	public void disconnect(String username){
		byte [] byeStream;
		DatagramPacket pdu;
		// new Goodbye object
		Goodbye bye = new Goodbye(username);
		try{
			byeStream =((Message)bye).toArray();
			this.socketUDP.setBroadcast(true);
			pdu=new DatagramPacket(byeStream,byeStream.length,InetAddress.getLocalHost(),portUDP);//InetAddress.getByAddress(localNetwork),portUDP);
			this.bufferMessagesToSend.add(pdu);
			this.run();
		}catch (IOException e){
			e.printStackTrace();
		}		
	}
	
	public void sendText (){
		InetAddress recipient;
		Iterator <String> it;
		DatagramPacket pdu2send;
		String text2Send = ChatSystem.getModelText().getTextToSend();
		Text messageText = new Text(ChatSystem.getModelUsername().getUsername(),text2Send);
		try{
			byte[] messageStream = messageText.toArray();	
			it = ChatSystem.getModelGroupRecipient().getGroupRecipients().iterator();
			while(it.hasNext()){
				recipient = ChatSystem.getModelListUsers().getListUsers().get((String)it.next());
				pdu2send = new DatagramPacket(messageStream,messageStream.length,recipient,this.portUDP);
				this.socketUDP.send(pdu2send);
				this.run();
			}
		}catch(IOException ioExc){
			System.out.println("error : construction du stream message");
		}	
	}
	
	public void run(){
		int first = 0;
		Message receivedMsg;
		InetAddress ipRemoteAddr;
		// envoie d'un pdu
		if (this.bufferMessagesToSend.isEmpty() == false){
			try{
				socketUDP.send(this.bufferMessagesToSend.get(first));
				this.bufferMessagesToSend.remove(first);
			}catch (IOException sendExc){
				System.out.println("cannot send the message");
			}			
		}
		// on essaie de recevoir un pdu
		try {
			this.socketUDP.receive(pduReceived);
		}catch (IOException sockRec){
			System.out.println("error receive socket");
		}
		// si le pdu n'est pas vide on traite le message recu
		if (this.pduReceived.getLength() > 0){
			ipRemoteAddr = pduReceived.getAddress();
			try {
				receivedMsg = Message.fromArray(pduReceived.getData());
				// si c'est un hello on fait le signale au controller
				if (receivedMsg.getClass() == Hello.class){
					ChatSystem.getController().connectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr), ipRemoteAddr,((Hello)receivedMsg).isAck());
				}else if(receivedMsg.getClass() == Text.class){
					ChatSystem.getController().messageReceived(((Text)receivedMsg).getText(), this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
				}
			}catch (IOException recExc){
				System.out.println("error fromArray receive");
			}			
		}
	}
	
	public  String makeUsername(String username, InetAddress ip){		
		return username +"@"+ ip.getHostAddress();
	}
	
}
