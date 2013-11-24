/**
 * 
 */
package chatSystemNetwork;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import chatSystemCommon.FilePart;

/**
 * @author joanna
 *
 */
public class ChatNIStreamConnection extends Thread{
	
	private ServerSocket serverSocket;
	private ArrayBlockingQueue<FilePart> fParts;
	
	public ChatNIStreamConnection(){
		try {
			this.serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			System.out.println("Error : server socket not created");
			e.printStackTrace();
		}		
	}
	
	public int getNumPort(){
		return this.serverSocket.getLocalPort();
	}
	
	
	public void setParts(ArrayBlockingQueue<FilePart> f){
		this.fParts = f;
	}
	
	public void run(){
		Socket socket;
		while(true){
			try {
				Thread.sleep(100);
				socket = this.serverSocket.accept();
				ChatNIStreamSender s = new ChatNIStreamSender(socket, this.fParts);			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error : accept in StreamConnection");
				e.printStackTrace();
			} 
		}
	}
}
