package org.insa.controller;

import java.net.InetAddress;

import org.insa.java.view.SendMessageNI;
import org.insa.model.User;
import org.java.factory.MessageFactory;

import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;

public class MessageController {
	private ChatController chatController;
	
	public MessageController(ChatController chatController) {
		this.chatController = chatController;
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
			chatController.addUser(user);
			if(!((Hello) msg).isAck())
				this.sendWelcomeMessage(MessageFactory.getHelloMessage(chatController.getLocalUsername(), true), user.getAddress());
		}
		else if(msg instanceof Goodbye) {
			chatController.removeUser(new User(user.getAddress(),msg.getUsername()));
		}
		else if(msg instanceof Text) {
			int selectedIndex = chatController.getSelectedIndex();
			chatController.addMessage(user, msg);
			chatController.getStatusBar().setTextLabel("Last message received from " + msg.getUsername());
			
			if(selectedIndex != -1)
				if(chatController.getUser(selectedIndex).equals(user))
					chatController.getTalk(selectedIndex);
		}
	}
}
