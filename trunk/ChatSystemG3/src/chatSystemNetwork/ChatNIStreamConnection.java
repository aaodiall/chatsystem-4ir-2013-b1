/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author alpha
 *
 */
public class ChatNIStreamConnection implements Runnable{

	private ServerSocket conSocket;
	private Socket socket;
	private ChatNIStreamSender senderTCP;
	private ChatNIStreamReceiver receiverTCP;
	private boolean sendMode;
	private boolean receiveMode;
	
	public ChatNIStreamConnection(int portTCP){
		this.sendMode = false;
		this.receiveMode = false;
		try {
			this.conSocket = new ServerSocket(portTCP);
		} catch (IOException e) {
			System.out.println("Error : server socket not created");
			e.printStackTrace();
		}
		
	}
	
	public int getNumPort(){
		return this.conSocket.getLocalPort();
	}
	
	public void run(){
		try {
			socket = this.conSocket.accept();
		} catch (IOException e) {
			System.out.println("Error : accept in StreamConnection");
			e.printStackTrace();
		}
	}
	
}
