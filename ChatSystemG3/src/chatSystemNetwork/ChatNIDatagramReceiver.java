/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ArrayBlockingQueue;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;


/**
 * This class is responsible for receiving UDP datagrams
 * @author joanna
 *
 */
public class ChatNIDatagramReceiver extends Thread{
	
	private ChatNI chatNI;
	private DatagramSocket socketUDP;
	private InetAddress userIP;
	private ArrayBlockingQueue <DatagramPacket> bufferPDUReceived;
	private byte[] streamReceived;
	private DatagramPacket pduReceived;
	private boolean connected;
	
	public ChatNIDatagramReceiver(ChatNI chatNI,DatagramSocket socketUDP, InetAddress userIP){
		this.connected = true;
		this.chatNI = chatNI;
		this.socketUDP = socketUDP;
		this.userIP = userIP;
		int bufferSize = 15;
		this.bufferPDUReceived = new ArrayBlockingQueue<DatagramPacket>(bufferSize) ;
		try {
			this.streamReceived = new byte[this.socketUDP.getReceiveBufferSize()];
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.pduReceived = new DatagramPacket(this.streamReceived,this.streamReceived.length);
	}	

	/**
	 * change the state to connected or disconnected
	 * @param isConnected
	 */
	void setConnected(boolean isConnected){
		this.connected = isConnected;
	}	
	
	/**
	 * analyze all the pdu UDP received and transmit information to controller
	 */
	public void pduAnalyze(){
		Message receivedMsg;
		InetAddress ipRemoteAddr;
		ipRemoteAddr = this.bufferPDUReceived.peek().getAddress();
		DatagramPacket pdureceived;
		if (!this.userIP.equals(ipRemoteAddr)){
			try {
				pdureceived = this.bufferPDUReceived.poll();
				receivedMsg = Message.fromArray(pdureceived.getData());
				String username = this.makeUsername(receivedMsg.getUsername(), ipRemoteAddr);
				// Hello
				if (receivedMsg.getClass() == Hello.class){
					this.chatNI.helloReceived(username, ipRemoteAddr, ((Hello)receivedMsg).isAck());
				// Text	
				}else if(receivedMsg.getClass() == Text.class){
					this.chatNI.textReceived(username, ((Text)receivedMsg).getText());
				// Goodbye
				}else if(receivedMsg.getClass() == Goodbye.class){
					this.chatNI.goodbyeReceived(username);
				// FileTransfertDemand
				}else if (receivedMsg.getClass() == FileTransfertDemand.class){
					FileTransfertDemand ftd = ((FileTransfertDemand)receivedMsg);
					this.chatNI.fileTansfertDemandReceived(username, ftd.getName(), ftd.getSize(), ftd.getIdDemand(),ftd.getPortClient());
				// FileTransfertConfirmation
				}else if (receivedMsg.getClass() == FileTransfertConfirmation.class){
					FileTransfertConfirmation ftco = ((FileTransfertConfirmation)receivedMsg);
					this.chatNI.fileTansfertConfirmationReceived(username, ftco.getIdDemand(), ftco.isAccepted());
				// FileTransfertCancel
				}else if (receivedMsg.getClass() == FileTransfertCancel.class){
					receivedMsg = null;
					//FileTransfertCancel ftca = ((FileTransfertCancel)receivedMsg);
					//this.chatNI.fileTansfertCancelReceived(username, ftca.getIdDemand());
				}
			}catch (IOException recExc){
				System.err.println("error : cannot transform PDUdata in Message");
				recExc.printStackTrace();
			}
		}else{
			this.bufferPDUReceived.poll();
		}
	}
	
	/**
	 * build a unique user name to use in the chat system 
	 * @param username remote user name
	 * @param ip remote IP address
	 * @return unique username to use in the chat system
	 */
	public String makeUsername(String username, InetAddress ip){		
		return username +"@"+ ip.getHostAddress();
	}
	
	/**
	 * receives datagrams and launch the analyze 
	 */
	@Override
	public void run(){
		try{
			this.socketUDP.setSoTimeout(30000);
		}catch(SocketException e){
			System.err.println("Error : Timeout not set");
		}
		while(this.connected){
			// on se met en attente de reception d'un pdu
			try {
				this.socketUDP.receive(pduReceived);
				// on a recu un pdu donc on traite le message qu'il contient
				this.bufferPDUReceived.add(pduReceived);
				this.pduAnalyze();
			}catch (SocketTimeoutException e) {
				//si le timeout est atteint le thread dort un peut
				try{
					Thread.sleep(1000);
				}catch(InterruptedException ie){
					System.err.println("Error: ChatNIDatagramReceiver has been interrupted");
				}				
			}catch (IOException sockRec){
				System.err.println("Error : ChatNIDatagramReceiver socket - fonction receive");	
				sockRec.printStackTrace();
			}
		}
		this.socketUDP.close();
	}

}
