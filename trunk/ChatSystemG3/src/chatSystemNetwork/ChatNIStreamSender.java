/**
 * 
 */
package chatSystemNetwork;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import chatSystemCommon.FilePart;

/**
 * @author joanna
 *
 */
public class ChatNIStreamSender extends Thread{
	
	private ServerSocket serverSocket;
	private Socket sock;
	private ObjectOutputStream out;
	private boolean firstPart;
	private int idDemand;
	private ChatNI chatNI;
	
	public ChatNIStreamSender(ChatNI chatNI, int idDemand){
		try {
			this.serverSocket = new ServerSocket(0);
			this.idDemand = idDemand;
			this.chatNI = chatNI;
			this.firstPart = true;
			System.out.println("port du server "+this.serverSocket.getLocalPort());
		} catch (IOException e) {
			System.err.println("Error : server socket not created");
			e.printStackTrace();
		}		
	}
	
	public int getNumPort(){
		return this.serverSocket.getLocalPort();
	}
	
	public void sendPart(FilePart f){
		try {
			if(this.firstPart){
			 	this.out = new ObjectOutputStream(new BufferedOutputStream(this.sock.getOutputStream()));
				this.firstPart=false;
			}
			this.out.reset();
			this.out.writeObject((Object)f);
			this.out.flush();
			if (f.isLast()){
				this.out.close();
				this.sock.close();
				this.chatNI.fileSent(idDemand);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	public void run(){
		try {
			this.sock = this.serverSocket.accept();
			this.serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error : accept in StreamConnection");
			e.printStackTrace();
		}
	}
}