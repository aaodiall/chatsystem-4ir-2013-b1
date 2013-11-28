/**
 * 
 */
package chatSystemNetwork;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import chatSystemCommon.FilePart;

/**
 * @author joanna
 *
 */
public class ChatNIStreamConnection extends Thread{
	
	private ServerSocket serverSocket;
	private ArrayBlockingQueue<FilePart> fParts;
	private ArrayList<ChatNIStreamSender> lsenders;
	private int maxTransferts;
	
	public ChatNIStreamConnection(){
		try {
			this.serverSocket = new ServerSocket(0);
			lsenders = new ArrayList<ChatNIStreamSender> (maxTransferts);
		} catch (IOException e) {
			System.out.println("Error : server socket not created");
			e.printStackTrace();
		}		
	}
	
	public void setMaxTransferts(int max){
		this.maxTransferts = max;
	}
	
	public int getNumPort(){
		return this.serverSocket.getLocalPort();
	}
	
	
	public void setParts(ArrayBlockingQueue<FilePart> f){
		this.fParts = f;
	}
	
	public void addSender(ChatNIStreamSender sender){
		if((this.lsenders != null) &&(this.lsenders.size() < this.maxTransferts)){
			this.lsenders.add(sender);
		}
	}
	
	public void checkSends(){
		int i; 
		for (i=0; i<this.lsenders.size();i++){
			if (this.lsenders.get(i).getIsSent()){
				
			}
		}
	}
	
	public void run(){
		Socket socket;
		try {
			System.out.println("avant accept");
			socket = this.serverSocket.accept();
			System.out.println("après accept");
			ChatNIStreamSender s = new ChatNIStreamSender(socket, this.fParts);
			this.lsenders.add(s);
			s.start();
		} catch (IOException e) {
			System.out.println("Error : accept in StreamConnection");
			e.printStackTrace();
		} 
	}
}
