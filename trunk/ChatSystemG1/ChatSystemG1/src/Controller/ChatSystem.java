package Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ChatNI.ChatNetwork;

public class ChatSystem {

	public ChatSystem() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws InterruptedException, UnknownHostException {
		// TODO Auto-generated method stub
		
		ChatController chatC = new ChatController();
		chatC.setLocalUsername("Aymeric@"+InetAddress.getLocalHost().getHostAddress());
		
		chatC.PerformConnect();
		Thread.sleep(1000);
		String Userdest[] = new String[1];
		Userdest[0] = "Aymeric@"+InetAddress.getLocalHost().getHostAddress();
		chatC.PerformSendMessage("coucou ca va?", Userdest);
		Thread.sleep(500);
		chatC.PerformDisconnect();
		
	}

}
