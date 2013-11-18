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

public class ChatNIMessage implements Runnable{
	
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
	private InetAddress userIPBroadcast;
	private ArrayBlockingQueue <DatagramPacket> bufferMsg2Send;
	private int bufferSize;
	
	public ChatNIMessage(int bufferSize, DatagramSocket socketChatNIMessage, ModelListUsers modelListUsers,InetAddress userIPBroadcast){
		this.socketUDP = socketChatNIMessage;
		this.bufferSize = bufferSize;
		// Enable Broadcast
		try{
			this.socketUDP.setBroadcast(true);
			this.modelListUsers = modelListUsers;
			this.userIPBroadcast=userIPBroadcast;
			this.bufferMsg2Send = new ArrayBlockingQueue<DatagramPacket>(this.bufferSize);
		}catch (SocketException e){
			System.out.println("error : socket exception setBroadcast");
		}		
	}
	
	public void sendHello(String username, boolean ack){
		byte [] helloStream;
		DatagramPacket pdu;
		// new Hello object
		Hello hello = new Hello(username,ack);
		System.out.println("dans sendHello");
		try {
			// Objet to byte[]
			helloStream = hello.toArray();
			// make pdu
			pdu = new DatagramPacket(helloStream,helloStream.length,this.userIPBroadcast,this.socketUDP.getLocalPort());
			// add pdu to bufferMessagesToSend
			this.bufferMsg2Send.add(pdu);
			System.out.println("size of Sbuffer : " + this.bufferMsg2Send.size());
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
			byeStream =bye.toArray();
			pdu=new DatagramPacket(byeStream, byeStream.length, this.userIPBroadcast, this.socketUDP.getLocalPort());
			this.bufferMsg2Send.add(pdu);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void sendText(InetAddress recipient, String text2Send,String username){
		//InetAddress recipient;
		//Iterator <String> it;
		DatagramPacket pdu2send;
		Text messageText = new Text(username,text2Send);
		try{
			byte[] messageStream = messageText.toArray();	
			//it = ChatSystem.getModelGroupRecipient().getGroupRecipients().iterator();
			//it = usernameList.iterator();
			//while(it.hasNext()){
				//recipient = this.modelListUsers.getListUsers().get((String)it.next());
				pdu2send = new DatagramPacket(messageStream,messageStream.length,recipient,this.socketUDP.getLocalPort());
				this.socketUDP.send(pdu2send);
			//}
		}catch(IOException ioExc){
			System.out.println("error : construction du stream message");
			ioExc.printStackTrace();
		}
	}
	
	public void sendFileTransfertDemand(String username, String name, long size,int PortClient){
		//FileTransfertDemand demand = new FileTransfertDemand();
		byte[] demandStream;
		InetAddress ipRemoteAddr;
		DatagramPacket pdu2send;
		/*try{
			demandStream = demand.toArray();
			pdu2send = new DatagramPacket(demandStream,demandStream.length,ipRemoteAddr,this.socketUDP.getLocalPort());
			this.socketUDP.send(pdu2send);
		}catch(IOException ioExc){
			System.out.println("error : construction du fileTransfertDemand");
			ioExc.printStackTrace();
		}*/
	}
	
	public void sendFileTransfertConfirmation(String recipient, String fileName, boolean response){
		
	}
	
	public void sendTransfertCancel(String recipient, String fileName){
		
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