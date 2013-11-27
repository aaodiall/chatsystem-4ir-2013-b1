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
import chatSystemModel.ModelUsername;
import chatSystemCommon.*;
import chatSystemController.Controller;

/* Note pour plus tard : 
 *  - decider si on utilise a chaque fois un seul objet hello,bye,text juste en modifiant les attributs
 *  - ecrire la javadoc et les commentaires
 */


public class ChatNI extends View implements Runnable, Observer{	
	
	private Controller controller;
	private ChatNIMessage chatNIMessage;
	private ChatNIStreamConnection server;
	private DatagramSocket socketUDP;
	private InetAddress userIP;
	private InetAddress userIPBroadcast;
	private ArrayBlockingQueue <DatagramPacket> bufferPDUReceived;
	private byte[] streamReceived;
	private DatagramPacket pduReceived;
	private ArrayList<ChatNIStreamSender> lsenders; 
	private NICache cache;

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
		lsenders = new ArrayList<ChatNIStreamSender> (5); 
		this.chatNIMessage = new ChatNIMessage(numMsgMax,this.socketUDP);
		this.chatNIMessage.start();
		this.server = new ChatNIStreamConnection();
		this.server.start();
	}
	
	/**
	 * 
	 * @param port port UDP de reception et d'emission
	 * @param numMsgMax nombre de pdu UDP qu'on peut stocker au maximum
	 */
	public void setUDPsocket(int port, int numMsgMax){
		try {
			this.socketUDP = new DatagramSocket(port);
			this.bufferPDUReceived = new ArrayBlockingQueue<DatagramPacket>(numMsgMax) ;
			this.streamReceived = new byte[this.socketUDP.getReceiveBufferSize()];
			this.pduReceived = new DatagramPacket(this.streamReceived,this.streamReceived.length);
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
	
	public void sendPropositionFile(InetAddress recipient, String fileName, long size, int idDemand){
		this.chatNIMessage.sendFileTransfertDemand(this.cache.getUsername(),recipient,fileName, size,server.getNumPort(),idDemand);
		System.out.println("dans sendMsgText Chatni");
	}
	
	public void sendConfirmationFile(InetAddress recipient, String fileName,boolean answer, int idDemand){
		this.chatNIMessage.sendFileTransfertConfirmation(this.cache.getUsername(),recipient, answer, idDemand);
	}
	
	public void sendMsgFile(ArrayBlockingQueue<byte[]> parts,int idDemand){
		ArrayBlockingQueue <FilePart> f = new ArrayBlockingQueue<FilePart>(parts.size());
		int i;
		for (i=0;i<(parts.size()-1);i++){
			FilePart p = new FilePart(this.cache.getUsername(),parts.poll(),false);
			f.add(p);
		}
		FilePart p = new FilePart(this.cache.getUsername(),parts.poll(),true);
		f.add(p);
		this.server.setParts(f);
	}
	
	public void pduAnalyze(){
		Message receivedMsg;
		InetAddress ipRemoteAddr;
		ipRemoteAddr = this.bufferPDUReceived.peek().getAddress();
		DatagramPacket pdureceived;
		if (this.userIP.equals(ipRemoteAddr)){
			try {
				pdureceived = this.bufferPDUReceived.poll();
				receivedMsg = Message.fromArray(pdureceived.getData());
				// si c'est un hello on fait le signale au controller
				if (receivedMsg.getClass() == Hello.class){
					controller.connectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr), ipRemoteAddr,((Hello)receivedMsg).isAck());
				}else if(receivedMsg.getClass() == Text.class){
					System.out.println("dans PDU ANALYSE TEXT RECU");
					controller.messageReceived(((Text)receivedMsg).getText(), this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
				}else if(receivedMsg.getClass() == Goodbye.class){
					controller.disconnectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
				}else if (receivedMsg.getClass() == FileTransfertDemand.class){
					FileTransfertDemand ftd = ((FileTransfertDemand)receivedMsg);
					controller.filePropositionReceived(ftd.getUsername(),ftd.getName(),ftd.getSize());
				}else if (receivedMsg.getClass() == FileTransfertConfirmation.class){
					FileTransfertConfirmation ftco = ((FileTransfertConfirmation)receivedMsg);
					controller.fileAnswerReceived(ftco.getUsername(),ftco.getIdDemand(),ftco.isAccepted());
				}else if (receivedMsg.getClass() == FileTransfertCancel.class){
					FileTransfertCancel ftca = ((FileTransfertCancel)receivedMsg);
					controller.fileTranfertCancelReceived(ftca.getUsername(),ftca.getIdDemand());
				}else if (receivedMsg.getClass()==FilePart.class){
					System.out.println("je suis dans received filepart");
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
				Thread.sleep(60);
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


	public void update(Observable arg0, Object arg1) {
		//s'il y a eu un setUsername
		if(arg0.getClass() == ModelUsername.class){
			this.cache.setUsername((String)arg1);
		}/*else
		// si il y a eu un setStateConnected
		if(arg0.getClass().equals(ModelStates.class)){
			// utilisateur deconnecte
			if (arg1.equals(false)){
				this.disconnect(ChatSystem.getModelUsername().getUsername());
			}
		}*/
	}

}