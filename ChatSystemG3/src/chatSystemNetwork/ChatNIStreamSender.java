/**
 * 
 */
package chatSystemNetwork;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import chatSystemCommon.FilePart;

/**
 * @author alpha
 *
 */
public class ChatNIStreamSender extends Thread{
	
	private BufferedOutputStream out;
	private ArrayBlockingQueue<FilePart> fParts;
	
	public ChatNIStreamSender(Socket socket,ArrayBlockingQueue<FilePart> parts){
		this.fParts = parts;
		try {
			this.out = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {			
			while (!this.fParts.isEmpty()){
				this.out.write(this.fParts.poll().toArray());
				this.out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
