/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Text;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author alpha
 * This class is responsible for sending all UDP datagram
 * The only messages that it can't send are FilePart messages
 */

public class ChatNIDatagramSender extends Thread{
	
	private DatagramSocket socketUDP;
	private ArrayBlockingQueue <DatagramPacket> bufferMsg2Send;
	private int numMsgMax;
	private boolean connected;
	
	public ChatNIDatagramSender(int numMsgMax, DatagramSocket socketChatNIMessage){
		this.socketUDP = socketChatNIMessage;
		this.numMsgMax = numMsgMax;
		this.connected = true;
		// Enable Broadcast
		try{
			this.socketUDP.setBroadcast(true);
			this.bufferMsg2Send = new ArrayBlockingQueue<DatagramPacket>(this.numMsgMax);
		}catch (SocketException e){
			System.err.println("Error : cannot set socket in a broadcast mode");
			System.exit(-1);
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
			System.err.println("Error : toArray Message");
			e.printStackTrace();
		}

	}
	
	void setConnected(boolean isConnected){
		this.connected = isConnected;
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
			byeStream = bye.toArray();
			pdu=new DatagramPacket(byeStream, byeStream.length,broadcast, this.socketUDP.getLocalPort());
			this.bufferMsg2Send.add(pdu);
		}catch (IOException e){
			System.err.println("Error : toArray Message");
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
		DatagramPacket pdu2send;
		Text messageText = new Text(username,text2Send);
		try{
			byte[] messageStream = messageText.toArray();	
			pdu2send = new DatagramPacket(messageStream,messageStream.length,recipient,this.socketUDP.getLocalPort());
			this.socketUDP.send(pdu2send);
		}catch(IOException e){
			System.err.println("Error : toArray Message");
			e.printStackTrace();
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
		}catch(IOException e){
			System.err.println("Error : toArrayMessage");
			e.printStackTrace();
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
		}catch(IOException e){
			System.err.println("Error : toArray Message");
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(this.connected){
			// il dort
			try{
				Thread.sleep(100);
				// envoie d'un pdu si le buffer n'est pas vide
				if (this.bufferMsg2Send.isEmpty() == false){
					try{
						this.socketUDP.send(this.bufferMsg2Send.poll());			
					}catch (IOException e){
						System.err.println("Error : cannot send the message");
						e.printStackTrace();
					}
				}
			}catch (InterruptedException e) {
				System.err.println("Error : sleep interrupted in DS-Thread");
				e.printStackTrace();
			}			
		}
	}	
}