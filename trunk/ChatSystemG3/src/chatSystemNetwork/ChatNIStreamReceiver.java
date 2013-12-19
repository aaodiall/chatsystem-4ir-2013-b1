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
 * This class is responsible for file receiving
 * @author joanna
 * 
 */
public class ChatNIStreamReceiver extends Thread{

	private Socket rSocket;
	private int remotePort;
	private InetAddress remoteIP;
	private ChatNI chatNI;
	private int idDemand;
	
	/**
	 * Constructor
	 * @param chatNI
	 * @param remotePort
	 * @param remoteIP
	 * @param idDemand
	 */
	public ChatNIStreamReceiver(ChatNI chatNI, int remotePort,InetAddress remoteIP,int idDemand){
		this.idDemand = idDemand;
		this.chatNI = chatNI;
		this.remotePort = remotePort;
		this.remoteIP = remoteIP;
	}
	
	/**
	 * 
	 * @return demand id associated
	 */
	public int getidDemand(){
		return this.idDemand;
	}
	
	/**
	 * connect to the server and read all the file parts
	 */
	@Override
	public void run(){
		Object objectRead;
		FilePart f;
		boolean isReceived = false;
		try {
			// creation du socket
			this.rSocket = new Socket(this.remoteIP,this.remotePort);
			// creation de l'inputstream (object pour simplifier les envois, buffered pour les performances)
			ObjectInputStream objectReader = new ObjectInputStream(new BufferedInputStream(this.rSocket.getInputStream()));
			// tant qu'on n'a pas tout recu on lit une partie et on la communique au chatNI
			// une fois qu'on a lu la derniere partie on ferme l'inputstream et on finit le run
			while (isReceived == false){
				objectRead = objectReader.readObject();
				f = (FilePart)objectRead;
				this.chatNI.filePartReceived(f.getFilePart(), this.idDemand, f.isLast());
				if (f.isLast() == true){
					isReceived = true;
					objectReader.close();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}	
}