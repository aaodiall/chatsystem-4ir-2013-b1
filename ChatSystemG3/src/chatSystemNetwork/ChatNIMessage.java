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

import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;
import chatSystemModel.ModelListUsers;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author alpha
 * Cette classe sert à envoyer des messages avec une connexion UDP
 * Il n'y a que les messages de type File qu'elle ne peut pas envoyer
 */

public class ChatNIMessage extends Thread{
	
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
	 * public void sendFileTransfertConfirmation(String recipient, String fileName, boolean answer);
	 * public void sendTransfertCancel(String recipient, String fileName);
	 * public void run();
	*/
	
	private DatagramSocket socketUDP;
	private ArrayBlockingQueue <DatagramPacket> bufferMsg2Send;
	private int numMsgMax;
	
	public ChatNIMessage(int numMsgMax, DatagramSocket socketChatNIMessage){
		this.socketUDP = socketChatNIMessage;
		this.numMsgMax = numMsgMax;
		// Enable Broadcast
		try{
			this.socketUDP.setBroadcast(true);
			this.bufferMsg2Send = new ArrayBlockingQueue<DatagramPacket>(this.numMsgMax);
		}catch (SocketException e){
			System.out.println("error : socket exception setBroadcast");
		}		
	}
	
	/**
	 * 
	 * @param username
	 * @param ack
	 * @param broadcast
	 */
	public void sendHello(Hello h, InetAddress broadcast){
		byte [] helloStream;
		DatagramPacket pdu;
		try {
			// Objet to byte[]
			helloStream = h.toArray();
			// make pdu
			pdu = new DatagramPacket(helloStream,helloStream.length,broadcast,this.socketUDP.getLocalPort());
			// add pdu to bufferMessagesToSend
			this.bufferMsg2Send.add(pdu);
		}catch (IOException e){
			System.out.println("connection failed");
		}

	}

	/**
	 * 
	 * @param username
	 * @param broadcast
	 */
	public void sendBye(String username, InetAddress broadcast){
		byte [] byeStream;
		DatagramPacket pdu;
		// new Goodbye object
		Goodbye bye = new Goodbye(username);
		try{
			byeStream =bye.toArray();
			pdu=new DatagramPacket(byeStream, byeStream.length,broadcast, this.socketUDP.getLocalPort());
			this.bufferMsg2Send.add(pdu);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param recipient
	 * @param text2Send
	 * @param username
	 */
	public void sendText(InetAddress recipient, String text2Send,String username){
		//InetAddress recipient;
		//Iterator <String> it;
		DatagramPacket pdu2send;
		Text messageText = new Text(username,text2Send);
		System.out.println("dans sendText ChatniMessage avant try");
		try{
			byte[] messageStream = messageText.toArray();	
			//it = ChatSystem.getModelGroupRecipient().getGroupRecipients().iterator();
			//it = usernameList.iterator();
			//while(it.hasNext()){
				//recipient = this.modelListUsers.getListUsers().get((String)it.next());
				pdu2send = new DatagramPacket(messageStream,messageStream.length,recipient,this.socketUDP.getLocalPort());
				this.socketUDP.send(pdu2send);
				System.out.println("dans sendText ChatniMessage");
			//}
		}catch(IOException ioExc){
			System.out.println("error : construction du stream message");
			ioExc.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param recipient
	 * @param name
	 * @param size
	 * @param portTCPLocal
	 */
	public void sendFileTransfertDemand(String username,InetAddress recipient,String name,long size,int portTCPLocal, int idDemand){		
		FileTransfertDemand demand = new FileTransfertDemand(username,name,size,portTCPLocal,idDemand);
		byte[] demandStream;
		DatagramPacket pdu2send;
		try{
			demandStream = demand.toArray();
			pdu2send = new DatagramPacket(demandStream,demandStream.length,recipient,this.socketUDP.getLocalPort());
			this.socketUDP.send(pdu2send);
		}catch(IOException ioExc){
			System.out.println("error : construction du fileTransfertDemand");
			ioExc.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param recipient
	 * @param answer
	 * @param idDemand
	 */
	public void sendFileTransfertConfirmation(String username, InetAddress recipient, boolean answer, int idDemand){
		FileTransfertConfirmation conf = new FileTransfertConfirmation(username,answer, idDemand);
		byte[] confStream;
		DatagramPacket pdu2send;
		try{
			confStream = conf.toArray();
			pdu2send = new DatagramPacket(confStream,confStream.length,recipient,this.socketUDP.getLocalPort());
			this.socketUDP.send(pdu2send);
			System.out.println("confirmation sent");
		}catch(IOException ioExc){
			System.out.println("error : construction du fileTransfertDemand");
			ioExc.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param recipient
	 * @param idDemand
	 */
	public void sendTransfertCancel(String username,InetAddress recipient,int idDemand){
		FileTransfertCancel cancel = new FileTransfertCancel(username,idDemand);
		byte[] cancelStream;
		DatagramPacket pdu2send;
		try{
			cancelStream = cancel.toArray();
			pdu2send = new DatagramPacket(cancelStream,cancelStream.length,recipient,this.socketUDP.getLocalPort());
			this.socketUDP.send(pdu2send);
		}catch(IOException ioExc){
			System.out.println("error : construction du fileTransfertDemand");
			ioExc.printStackTrace();
		}
	}
	
	public void run(){
		System.out.println("thread send active");
		while(true){
			// il dort
			try{
				Thread.sleep(100);
				// envoie d'un pdu si le buffer n'est pas vide
				if (this.bufferMsg2Send.isEmpty() == false){
					try{
						this.socketUDP.send(this.bufferMsg2Send.poll());
						System.out.println("message envoyé");				
					}catch (IOException sendExc){
						System.out.println("cannot send the message");
						sendExc.printStackTrace();
					}
				}
			}catch (InterruptedException e) {
				System.out.println("sleep interrupted in S-Thread");
				e.printStackTrace();
			}			
		}
	}	
}