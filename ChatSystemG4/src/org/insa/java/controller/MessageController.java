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

/**
 * Controller concerning messages.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public class MessageController {
	private ChatController chatController;
	private JavaChatGUI chatGUI;
	private ChatModel chatModel;
	
	/**
	 * Constructor
	 * @param chatController Generic controler.
	 * @param chatGUI Main graphic interface.
	 * @param chatModel Data model. 
	 */
	public MessageController(ChatController chatController, JavaChatGUI chatGUI, ChatModel chatModel) {
		this.chatController = chatController;
		this.chatGUI = chatGUI;
		this.chatModel = chatModel;
	}

	/**
	 * Send a hello message.
	 * @param helloMessage Message to send.
	 */
	public void sendHelloMessage(Hello helloMessage) {
		SendMessageNI.getInstance().sendBroadcastMessage(helloMessage);
	}
	
	/**
	 * Send a bye message.
	 * @param byeMessage Message to send.
	 */
	public void sendGoodbyeMessage(Goodbye byeMessage) {
		SendMessageNI.getInstance().sendBroadcastMessage(byeMessage);
	}
	
	/**
	 * Send a welcome message to a destination user.
	 * @param helloMessage Message to send.
	 * @param address Address of the destination user.
	 */
	public void sendWelcomeMessage(Hello helloMessage, InetAddress address) {
		SendMessageNI.getInstance().sendMessage(helloMessage, address);
	}

	/**
	 * Receive method concerning messages.
	 * @param user User who sent the message.
	 * @param msg Received message.
	 */
	public void receivedMessage(User user, Message msg) {
		if(msg instanceof Hello) {
			if(!chatModel.contains(user)) 
				chatModel.addUser(user);
			if(!((Hello) msg).isAck())
				this.sendWelcomeMessage(MessageFactory.hello(chatModel.getLocalUsername(), true), user.getAddress());
		}
		else if(msg instanceof Goodbye) {
			chatModel.removeUser(new User(user.getAddress(),msg.getUsername()));
		}
		else if(msg instanceof Text) {
			int selectedIndex = chatGUI.getSelectedUserIndex();
			this.addMessage(user, msg);
			chatGUI.displayMessageInformation("New message received from : " + msg.getUsername());
			
			if(selectedIndex != -1)
				if(chatModel.get(selectedIndex).equals(user))
					chatController.updateTalk(selectedIndex);
		}
	}

	/**
	 * Construct talk from selected user index, by accessing messages in chatModel.
	 * @param index Selected user index.
	 */
	public void updateTalk(int index) {
		User u = chatModel.get(index);
		String talk = "";
		
		for(Message m : u.getMessageList()) {
			if(m instanceof Text)
				talk += m.getUsername() + " : " + ((Text) m).getText() + "\n";
		}
		
		chatGUI.displayTalk(talk);
	}
	
	/**
	 * Add a message a talk with a destination user.
	 * @param user Destination user.
	 * @param msg Message to add.
	 */
	public void addMessage(User user, Message msg) {
		chatModel.get(user).addMessage(msg);
	}
}
