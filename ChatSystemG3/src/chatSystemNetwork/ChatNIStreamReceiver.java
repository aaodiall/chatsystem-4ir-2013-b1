/**
 * 
 */
package chatSystemNetwork;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author joanna
 *
 */
public class ChatNIStreamReceiver extends Thread{

	private Socket rSocket;
	private int remotePort;
	private InetAddress remoteIP;
	private BufferedInputStream reader;
	
	// bufferSize = 1024
	public ChatNIStreamReceiver(int remotePort,InetAddress RemoteIP){
		this.remotePort = remotePort;
		this.remoteIP = remoteIP;
	}

	
	public void run(){
		try {
			this.rSocket = new Socket(this.remoteIP,this.remotePort);
			this.reader = new BufferedInputStream(this.rSocket.getInputStream());			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
