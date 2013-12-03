package org.insa.java.controller;

import java.net.InetAddress;

import org.insa.java.factory.MessageFactory;
import org.insa.java.model.ChatModel;
import org.insa.java.model.User;
import org.insa.java.view.JavaChatGUI;
import org.insa.java.view.SendMessageNI;

import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;

public class MessageController {
	private ChatController chatController;
	private JavaChatGUI chatGUI;
	private ChatModel chatModel;
	
	public MessageController(ChatController chatController, JavaChatGUI chatGUI, ChatModel chatModel) {
		this.chatController = chatController;
		this.chatGUI = chatGUI;
		this.chatModel = chatModel;
	}

	public void sendHelloMessage(Hello helloMessage) {
		SendMessageNI.getInstance().sendBroadcastMessage(helloMessage);
	}
	
	public void sendGoodbyeMessage(Goodbye byeMessage) {
		SendMessageNI.getInstance().sendBroadcastMessage(byeMessage);
	}
	
	public void sendWelcomeMessage(Hello helloMessage, InetAddress address) {
		SendMessageNI.getInstance().sendMessage(helloMessage, address);
	}

	public void receivedMessage(User user, Message msg) {
		if(msg instanceof Hello) {
			if(!chatModel.contains(user)) 
				chatModel.addUser(user);
			if(!((Hello) msg).isAck())
				this.sendWelcomeMessage(MessageFactory.getHelloMessage(chatModel.getLocalUsername(), true), user.getAddress());
		}
		else if(msg instanceof Goodbye) {
			chatModel.removeUser(new User(user.getAddress(),msg.getUsername()));
		}
		else if(msg instanceof Text) {
			int selectedIndex = chatGUI.getSelectedIndex();
			this.addMessage(user, msg);
			chatGUI.getStatusBar().setMessageBarText("New message received from : " + msg.getUsername());
			
			if(selectedIndex != -1)
				if(chatModel.get(selectedIndex).equals(user))
					chatController.updateTalk(selectedIndex);
		}
	}

	public void updateTalk(int index) {
		User u = chatModel.get(index);
		String talk = "";
		
		for(Message m : u.getMessageList()) {
			if(m instanceof Text)
				talk += m.getUsername() + " : " + ((Text) m).getText() + "\n";
		}
		
		chatGUI.setChatText(talk);
	}
	
	public void addMessage(User user, Message msg) {
		chatModel.get(user).addMessage(msg);
	}
}
