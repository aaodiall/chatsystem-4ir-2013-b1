/**
 * 
 */
package chatSystemNetwork;

import java.net.Socket;

/**
 * @author joanna
 *
 */
public class ChatNIStreamReceiver implements Runnable{

	private Socket rSocket;
	// bufferSize = 1024
	public ChatNIStreamReceiver(Socket rSocket){
		this.rSocket = rSocket;
	}
	
	public void run(){
		
	}
	
}
