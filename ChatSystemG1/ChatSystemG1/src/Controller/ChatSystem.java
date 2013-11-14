package Controller;

import java.net.UnknownHostException;


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
		
		
		while(chatC.getChatGuiFrontController().getmFrame().isVisible()){
			System.out.flush();
		}
		
		System.exit(0);
		
		
	}

}
