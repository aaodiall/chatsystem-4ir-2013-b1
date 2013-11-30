/**
 * 
 */
package chatSystemNetwork;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import chatSystemCommon.FilePart;

/**
 * 
 * @author joanna
 *	le Sender accepte la connection et envoie le fichier 
 */
public class ChatNIStreamSender extends Thread{
	
	private ObjectOutputStream out;
	private ArrayBlockingQueue<FilePart> fParts;
	private boolean isSent;
	
	public ChatNIStreamSender(Socket socket,ArrayBlockingQueue<FilePart> parts){
		this.fParts = parts;
		this.isSent = false;
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getIsSent(){
		return this.isSent;
	}
	
	public void run(){
		try {			
			System.out.println("sender actif");
			while (!this.fParts.isEmpty()){
				this.out.writeObject((Object)this.fParts.poll());
				this.out.flush();
			}
			System.out.println("end writing");
			this.isSent = true;
			this.out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
