/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;
import chatSystemModel.ModelListUsers;
import chatSystemModel.ModelUsername;

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
	private ModelUsername modelUsername;
	private InetAddress userIP;
	private InetAddress userIPBroadcast;
	private ArrayList<DatagramPacket> bufferMsg2Send;
	
	public ChatNIMessage(DatagramSocket socketChatNIMessage, int bufferSize, ModelListUsers modelListUsers,ModelUsername modelUsername,InetAddress userIP,InetAddress userIPBroadcast){
		this.socketUDP = socketChatNIMessage;
		// Enable Broadcast
		try{
			this.socketUDP.setBroadcast(true);
			this.bufferMsg2Send = new ArrayList<DatagramPacket>(this.socketUDP.getSendBufferSize());
		}catch (SocketException e){
			System.out.println("error : socket exception setBroadcast");
		}
		
		this.modelListUsers = modelListUsers;
		this.modelUsername = modelUsername;
		this.userIP=userIP;
		this.userIPBroadcast=userIPBroadcast;
	}
	
	public void sendHello(String username, boolean ack){
		byte [] helloStream;
		DatagramPacket pdu;
		// new Hello object
		Hello hello = new Hello(username,ack);
		try {
			// Objet to byte[]
			helloStream = ((Message)hello).toArray();
			// make pdu
			pdu = new DatagramPacket(helloStream,helloStream.length,this.userIPBroadcast,this.socketUDP.getLocalPort());
			// add pdu to bufferMessagesToSend
			this.bufferMsg2Send.add(pdu);
			// send pdu
			this.run();		
		}catch (IOException e){
			System.out.println("connection failed");
		}

	}

	public void sendBye(String username){
		byte [] byeStream;
		DatagramPacket pdu;
		// new Goodbye object
		Goodbye bye = new Goodbye(username);
		try{
			byeStream =((Message)bye).toArray();
			pdu=new DatagramPacket(byeStream, byeStream.length, this.userIPBroadcast, this.socketUDP.getLocalPort());
			this.bufferMsg2Send.add(pdu);
			this.run();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void sendText(ArrayList<String> usernameList, String text2Send,String username){
		InetAddress recipient;
		Iterator <String> it;
		DatagramPacket pdu2send;
		Text messageText = new Text(username,text2Send);
		try{
			byte[] messageStream = messageText.toArray();	
			//it = ChatSystem.getModelGroupRecipient().getGroupRecipients().iterator();
			it = usernameList.iterator();
			while(it.hasNext()){
				recipient = this.modelListUsers.getListUsers().get((String)it.next());
				pdu2send = new DatagramPacket(messageStream,messageStream.length,recipient,this.socketUDP.getPort());
				this.socketUDP.send(pdu2send);
				this.run();
			}
		}catch(IOException ioExc){
			System.out.println("error : construction du stream message");
		}
	}
	
	public void sendFileTransfertDemand(String recipient, String fileName){
		
	}
	
	public void sendFileTransfertConfirmation(String recipient, String fileName, boolean response){
		
	}
	
	public void sendTransfertCancel(String recipient, String fileName){
		
	}
	
	void run(){
		int first = 0;
		// envoie d'un pdu
		if (this.bufferMsg2Send.isEmpty() == false){
			try{
				socketUDP.send(this.bufferMsg2Send.get(first));
				this.bufferMsg2Send.remove(first);
			}catch (IOException sendExc){
				System.out.println("cannot send the message");
			}			
		}
	}	
}
