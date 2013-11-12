/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemModel.ModelListUsers;

/**
 * @author alpha
 * Cette classe sert à envoyer des messages avec une connexion UDP
 * Il n'y a que les messages de type File qu'elle ne peut pas envoyer
 */

public class ChatNIMessage {
	
	/* ATTRIBUTS A METTRE
	 * DatagramSocket socketUDP;
	 * ModelListUsers modelListUsers;
	 * InetAddress userIP;
	 * InetAddress userIPBroadcast;
	 * ArrayList<byte[]> bufferMsg2Send;
	 * 
	 * METHODES A IMPLEMENTER
	 * public ChatNIMessage(DatagramSocket socketUDP, ModelListUsers modelListUsers);
	 * public void sendHello(String username, boolean ack);
	 * public void sendBye();
	 * public void sendText(ArrayList<String> usernameList, String text2Send);
	 * public void sendFileTransfertDemand(String recipient, String fileName);
	 * public void sendFileTransfertConfirmation(String recipient, String fileName, boolean response);
	 * public void sendTransfertCancel(String recipient, String fileName);
	 * public void run();
	*/
	
	private DatagramSocket socketUDP;
	private ModelListUsers modelListUsers;
	private InetAddress userIP;
	private InetAddress userIPBroadcast;
	private ArrayList<DatagramPacket> bufferMsg2Send;
	
	public ChatNIMessage(DatagramSocket socketChatNIMessage, int bufferSize, ModelListUsers modelListUsers,InetAddress userIP,InetAddress userIPBroadcast){
		this.socketUDP = socketChatNIMessage;
		this.modelListUsers = modelListUsers;
		this.userIP=userIP;
		this.userIPBroadcast=userIPBroadcast;
		this.bufferMsg2Send = new ArrayList<DatagramPacket>(bufferSize);
	}
	
	public void sendHello(String username, boolean ack){
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
			pdu = new DatagramPacket(helloStream,helloStream.length,this.socketUDP.getLocalPort());
			// add pdu to bufferMessagesToSend
			this.bufferMsg2Send.add(pdu);
			// send pdu
			this.run();
			this.socketUDP.setBroadcast(false);			
		}catch (IOException e){
			System.out.println("connection failed");
		}

	}

	public void sendBye(){
/*		byte [] byeStream;
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
		}*/
	}
	
	public void sendText(ArrayList<String> usernameList, String text2Send){
		/*InetAddress recipient;
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
		}*/	
	}
	
	public void sendFileTransfertDemand(String recipient, String fileName){
		
	}
	
	public void sendFileTransfertConfirmation(String recipient, String fileName, boolean response){
		
	}
	
	public void sendTransfertCancel(String recipient, String fileName){
		
	}
	
	/*int first = 0;
	
	// envoie d'un pdu
	if (this.bufferMessagesToSend.isEmpty() == false){
		try{
			socketUDP.send(this.bufferMessagesToSend.get(first));
			this.bufferMessagesToSend.remove(first);
		}catch (IOException sendExc){
			System.out.println("cannot send the message");
		}			
	}*/
	
	void run(){
		
	}
	
}
