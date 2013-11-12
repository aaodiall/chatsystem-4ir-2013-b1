package Controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ChatNI.ChatNetwork;
import IHM.FrontController;


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
		FrontController fc = new FrontController();
		
		while(fc.getmFrame().isVisible()){
			System.out.flush();
		}
		System.out.println("on quitte!");
		System.exit(0);
		
		
	}

}
