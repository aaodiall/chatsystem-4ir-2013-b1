/**
 * 
 */
package chatSystemNetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * @author alpha
 *
 */
public class ChatNIStreamConnection {

	private ServerSocket sSocket;
	
	public ChatNIStreamConnection(int portTCP){
		try {
			this.sSocket = new ServerSocket(portTCP);
		} catch (IOException e) {
			System.out.println("Error : server socket not created");
			e.printStackTrace();
		}
		
	}
}
