package Controller;

import ChatNI.ChatNetwork;

public class ChatSystem {

	public ChatSystem() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ChatNetwork chatni = new ChatNetwork();
		ChatController chatC = new ChatController();
		chatC.setLocalUsername("Aymeric@192.168.1.27");
		chatC.setChatNi(chatni);
		
		
			
		chatC.PerformConnect();
		Thread.sleep(500);
		chatC.PerformDisconnect();
		
	}

}
