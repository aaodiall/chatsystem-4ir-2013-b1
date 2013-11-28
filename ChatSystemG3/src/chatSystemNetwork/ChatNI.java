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
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;
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
	private ArrayList<ChatNIStreamReceiver> lreceivers;
	private NICache cache;
	private int maxTransferts;

	/**
	 * 
	 * @param portUDP port UDP pour l'envoi et la recception des messages
	 * @param numMsgMax nombre de messages qu'on peut stocker au maximum
	 * @param controller controller du NI
	 */
	public ChatNI(int portUDP,int numMsgMax, Controller controller){
		this.maxTransferts = 5;
		this.controller=controller;
		this.cache = new NICache();
		this.setlocalIPandBroadcast();
		this.setUDPsocket(portUDP, numMsgMax);
		this.lreceivers = new ArrayList<ChatNIStreamReceiver> (this.maxTransferts);
		this.chatNIMessage = new ChatNIMessage(numMsgMax,this.socketUDP);
		this.chatNIMessage.start();
		this.server = new ChatNIStreamConnection();
		this.server.setMaxTransferts(this.maxTransferts);
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

	
	public void addReceiver(ChatNIStreamReceiver receiver){
		if((this.lreceivers != null) &&(this.lreceivers.size() < this.maxTransferts)){
			this.lreceivers.add(receiver);
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
		this.chatNIMessage.sendFileTransfertDemand(this.cache.getUsername(),recipient,fileName, size,this.server.getNumPort(),idDemand);
		this.cache.removePort(remote);
		this.cache.addPort(remote, this.server.getNumPort());
	}
	
	
	public void sendConfirmationFile(String recipientName, InetAddress recipientIP, String fileName,boolean answer, int idDemand, int numParts){
		// si l'utilisateur veut recevoir le fichier
		this.chatNIMessage.sendFileTransfertConfirmation(this.cache.getUsername(),recipientIP, answer, idDemand);
		if (answer == true){
			ChatNIStreamReceiver sr = new ChatNIStreamReceiver(this.cache.getPort(recipientName),recipientIP,numParts);
			this.addReceiver(sr);
			sr.start();
		}
	}
	
	
	public void sendMsgFile(ArrayBlockingQueue<byte[]> parts,int idDemand){
		ArrayBlockingQueue <FilePart> f = new ArrayBlockingQueue<FilePart>(parts.size());
		int i;
		if (parts.size() == 1){
			FilePart p = new FilePart(this.cache.getUsername(),parts.poll(),true);
			f.add(p);
		}else{
			for (i=0;i<(parts.size()-1);i++){
				FilePart p = new FilePart(this.cache.getUsername(),parts.poll(),false);
				f.add(p);
			}
			FilePart p = new FilePart(this.cache.getUsername(),parts.poll(),true);
			f.add(p);
		}
		this.server.setParts(f);
		this.server.start();
	}
	
	/**
	 * analyze all the pdu UDP received and transmit information to controller
	 */
	public void pduAnalyze(){
		Message receivedMsg;
		InetAddress ipRemoteAddr;
		ipRemoteAddr = this.bufferPDUReceived.peek().getAddress();
		DatagramPacket pdureceived;
		if (this.userIP.equals(ipRemoteAddr)){
			try {
				pdureceived = this.bufferPDUReceived.poll();
				receivedMsg = Message.fromArray(pdureceived.getData());
				// Hello
				if (receivedMsg.getClass() == Hello.class){
					controller.connectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr), ipRemoteAddr,((Hello)receivedMsg).isAck());
				// Text	
				}else if(receivedMsg.getClass() == Text.class){
					controller.messageReceived(((Text)receivedMsg).getText(), this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
				// Goodbye
				}else if(receivedMsg.getClass() == Goodbye.class){
					controller.disconnectReceived(this.makeUsername(receivedMsg.getUsername(),ipRemoteAddr));
				// FileTransfertDemand
				}else if (receivedMsg.getClass() == FileTransfertDemand.class){
					FileTransfertDemand ftd = ((FileTransfertDemand)receivedMsg);
					// on enregistre le port ouvert par le remote
					this.cache.addPort(this.makeUsername(ftd.getUsername(),ipRemoteAddr), ftd.getPortClient());
					this.cache.addDemand(ftd.getPortClient(), ftd.getIdDemand());
					controller.filePropositionReceived(this.makeUsername(ftd.getUsername(),ipRemoteAddr),ftd.getName(),ftd.getSize(),ftd.getIdDemand());
				// FileTransfertConfirmation
				}else if (receivedMsg.getClass() == FileTransfertConfirmation.class){
					FileTransfertConfirmation ftco = ((FileTransfertConfirmation)receivedMsg);
					controller.fileAnswerReceived(this.makeUsername(ftco.getUsername(),ipRemoteAddr),ftco.getIdDemand(),ftco.isAccepted());
				// FileTransfertCancel
				}else if (receivedMsg.getClass() == FileTransfertCancel.class){
					FileTransfertCancel ftca = ((FileTransfertCancel)receivedMsg);
					controller.fileTranfertCancelReceived(this.makeUsername(ftca.getUsername(),ipRemoteAddr),ftca.getIdDemand());
				}
			}catch (IOException recExc){
				System.out.println("error : cannot transform PDUdata in Message");
				recExc.printStackTrace();
			}
		}
	}
	
	public void checkReceives(){
		int i;
		int j;
		for (i=0;i<this.lreceivers.size();i++){
			if(this.lreceivers.get(i).getIsReceived()){
				ArrayList<byte[]> parts = this.lreceivers.get(i).getAllParts();
				System.out.println("1 file received!!!");
				for (j=0;j<parts.size()-1;j++){
					this.controller.partReceived(parts.get(j), this.cache.getDemand(this.lreceivers.get(i).getPort()), false);
				}
				this.controller.partReceived(parts.get(j), this.cache.getDemand(this.lreceivers.get(i).getPort()), true);
				this.lreceivers.remove(i);
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