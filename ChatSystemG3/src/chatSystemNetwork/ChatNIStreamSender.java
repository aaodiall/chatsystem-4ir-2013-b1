/**
 * 
 */
package chatSystemNetwork;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import chatSystemCommon.FilePart;

/**
 * 
 * @author joanna
 *	le Sender accepte la connection et envoie le fichier 
 */
public class ChatNIStreamSender extends Thread{
	
	private BufferedOutputStream out;
	private ArrayBlockingQueue<FilePart> fParts;
	private boolean isSent;
	
	public ChatNIStreamSender(Socket socket,ArrayBlockingQueue<FilePart> parts){
		this.fParts = parts;
		this.isSent = false;
		try {
			this.out = new BufferedOutputStream(socket.getOutputStream());
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
				System.out.println("before write");
				this.out.write(this.fParts.poll().toArray());
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
