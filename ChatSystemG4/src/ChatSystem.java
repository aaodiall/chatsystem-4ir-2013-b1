import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JList;

import Controller.ChatController;
import Model.ChatModel;
import Model.User;
import View.ReceivedNI;
import View.SendNI;


public class ChatSystem {
	
	public static void main(String[] args) {
		ChatModel chatModel = new ChatModel(); 
		
		JFrame fen = new JFrame();
		JList<User> list = new JList<User>();
		list.setModel(chatModel);
		fen.setLayout(new BorderLayout());
		fen.add(list,BorderLayout.CENTER);
		fen.setVisible(true);
		
		ChatController chatController = new ChatController(chatModel);
	}
}
