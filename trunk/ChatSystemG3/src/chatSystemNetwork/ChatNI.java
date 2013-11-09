/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import runChat.ChatSystem;
import chatSystemIHMs.View;
import chatSystemCommon.*;
import chatSystemController.*;

public class ChatNI extends View implements Runnable{
	
	private final int portUDP;
	private byte[] localNetwork;
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
		bufferMessagesToSend = new ArrayList<DatagramPacket>();
		try {
			// recuperation de l'IPUser
			localUserIP = InetAddress.getLocalHost();
			System.out.println("IP localUser : " + this.localUserIP.toString());
			// recuperation de l'IPLocalNetwork
			localNetwork = localUserIP.getAddress();
			localNetwork [localNetwork.length - 1] = (byte)0;
			System.out.println("IP reseau local : " + InetAddress.getByAddress(localNetwork).toString());
			// construction du socket UDP
			this.socketUDP = new DatagramSocket(portUDP,localUserIP);
			this.socketUDP.toString();
			// initialisation d'un pdu de reception
			this.streamReceived = new byte[this.socketUDP.getReceiveBufferSize()];
			this.pduReceived = new DatagramPacket(this.streamReceived,this.streamReceived.length);
			// initialisation de la liste des remoteUsers de ChatNI
			remoteUsersList = new HashMap<String,InetAddress>();
		}catch (UnknownHostException eAddr){
			System.out.println("connection failed : no Localhost");
		}catch(SocketException sockExc){
			System.out.println("socketUDP : socket exception");
			this.socketUDP.close();
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
			pdu = new DatagramPacket(helloStream,helloStream.length,InetAddress.getLocalHost(),portUDP);//InetAddress.getByAddress(localNetwork),portUDP);
			// add pdu to bufferMessagesToSend
			this.bufferMessagesToSend.add(pdu);
			// send pdu
			this.run();
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
					ChatSystem.getController().ConnectReceived(receivedMsg.getUsername(), ipRemoteAddr,((Hello)receivedMsg).isAck());
				}
			}catch (IOException recExc){
				System.out.println("error fromArray receive");
			}			
		}
	}
	public  String makeUsername(String username, InetAddress ip){
		String ipString[]=ip.toString().split("/");
		
		return username+"@"+ipString[1];
	}
	
}
