/**
 * 
 */
package chatSystemNetwork;

import java.net.Socket;

/**
 * @author joanna
 *
 */
public class ChatNIStreamSender implements Runnable{

	private Socket sSocket;
	
	public ChatNIStreamSender(Socket sSocket){
		this.sSocket = sSocket;
	}
	
	public void run(){
		
	}
}
//public void sendFile(String recipient, String){}