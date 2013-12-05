/**
 * 
 */
package chatSystemNetwork;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import chatSystemCommon.FilePart;

/**
 * @author joanna
 * le Receiver se connecte au port distant et reçoit le fichier
 */
public class ChatNIStreamReceiver extends Thread{

	private Socket rSocket;
	private int remotePort;
	private InetAddress remoteIP;
	private ChatNI chatNI;
	private int idDemand;
	
	public ChatNIStreamReceiver(ChatNI chatNI, int remotePort,InetAddress remoteIP,int idDemand){
		this.idDemand = idDemand;
		this.chatNI = chatNI;
		this.remotePort = remotePort;
		this.remoteIP = remoteIP;
	}
	
	
	public int getidDemand(){
		return this.idDemand;
	}
	

	public void run(){
		Object objectRead;
		FilePart f;
		boolean isReceived = false;
		try {
			this.rSocket = new Socket(this.remoteIP,this.remotePort);
			ObjectInputStream objectReader = new ObjectInputStream(new BufferedInputStream(this.rSocket.getInputStream()));
			while (isReceived == false){
				objectRead = objectReader.readObject();
				f = (FilePart)objectRead;
				this.chatNI.filePartReceived(f.getFilePart(), this.idDemand, f.isLast());
				if (f.isLast() == true){
					isReceived = true;
					objectReader.close();
				}
			}
			this.chatNI.fileReceived(this.idDemand);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	
}
