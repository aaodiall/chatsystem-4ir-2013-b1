package Controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
		java.io.File f = new File("testsend.txt");
		if(f == null){
			System.out.println("000");
		}
		chatC.PerformFileAcceptance(f, "Aymeric@"+InetAddress.getLocalHost().getHostAddress());
		Thread.sleep(1000);
		String Userdest[] = new String[1];
		Userdest[0] = "Aymeric@"+InetAddress.getLocalHost().getHostAddress();
		chatC.PerformSendMessage("coucou ca va?", Userdest);
		Thread.sleep(500);
		chatC.PerformDisconnect();
		
	}

}
