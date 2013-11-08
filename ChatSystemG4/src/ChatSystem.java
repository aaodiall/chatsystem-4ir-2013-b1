import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JList;

import org.junit.Test;

import chatSystemCommon.Hello;
import Controller.ChatController;
import Model.ChatModel;
import Model.User;
import View.SendNI;


public class ChatSystem {
	
	public static void main(String[] args) {
		
	}
	
	@Test
	public void sendHelloMessage() {
		SendNI.getInstance().sendBroadcastMessage(new Hello("thomas", false));
	}
	
	@Test
	public void receivedMessage() {
		ChatModel chatModel = new ChatModel(); 
		
		JFrame fen = new JFrame();
		JList<User> list = new JList<User>();
		list.setModel(chatModel);
		fen.setLayout(new BorderLayout());
		fen.add(list,BorderLayout.CENTER);
		fen.setVisible(true);
		
		new ChatController(chatModel);
	}
	
	
}
