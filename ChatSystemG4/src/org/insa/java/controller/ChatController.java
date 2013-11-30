package org.insa.java.controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

import org.insa.java.factory.MessageFactory;
import org.insa.java.model.ChatModel;
import org.insa.java.model.User;
import org.insa.java.view.JavaChatGUI;
import org.insa.java.view.ReceivedMessageNI;
import org.insa.java.view.SendMessageNI;

import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;

import com.sun.istack.internal.logging.Logger;

public class ChatController {
	private ChatModel chatModel;
	private JavaChatGUI chatGUI;
	private MessageController messageController;
	private FileController fileController;
	private Thread receivedThread;	
	
	public ChatController(JavaChatGUI chatGUI) {
		this.chatModel = null;
		this.chatGUI = chatGUI;
	}

	public void sendTextMessage(User selectedUser, String message) {
		Message text = new Text(chatModel.getLocalUser().getUsername(),message);
		chatModel.get(selectedUser).addMessage(text);
		SendMessageNI.getInstance().sendMessage(text,selectedUser.getAddress());
		this.getTalk(chatModel.indexOf(selectedUser));	
	}
	
	public void sendFile(User user, File file) {
		fileController.beginFileTransfertProtocol(user, file);
	}

	public void sendHelloMessage(User localUser) {
		messageController.sendHelloMessage(MessageFactory.getHelloMessage(chatModel.getLocalUser().getUsername(), false));
	}

	public void sendGoodbyeMessage(User localUser) {
		messageController.sendGoodbyeMessage(MessageFactory.getByeMessage(chatModel.getLocalUser().getUsername()));
	}
	
	public int receivedMessage(InetAddress inetAddress, Message msg) {
		try {
			if(inetAddress.equals(InetAddress.getLocalHost()))
				return -1;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		User user = new User(inetAddress,msg.getUsername());
		
		Logger.getLogger(ChatController.class).log(Level.INFO, "Message received >> " + msg.toString());
		
		if(msg instanceof Hello || msg instanceof Goodbye || msg instanceof Text) {
			messageController.receivedMessage(user,msg);
		}
		else {
			fileController.receivedMessage(user,msg);
		}
		return 0;
	}
	
	public void connect() {
		String localUsername = chatGUI.usernameInput();
		this.checkLocalUsername(localUsername);
		try {
			localUsername += "@" + InetAddress.getLocalHost().getHostAddress();
			chatModel = new ChatModel(new User(InetAddress.getLocalHost(),localUsername));
			this.messageController = new MessageController(this,chatGUI,chatModel);
			this.fileController = new FileController(this,chatGUI,chatModel);
		} catch (UnknownHostException e) {
			Logger.getLogger(JavaChatGUI.class).log(Level.SEVERE, null, e);
		}
		this.enableMessageReception();
		this.sendHelloMessage(chatModel.getLocalUser());	
	}
	
	public void enableMessageReception() {
		receivedThread = new Thread(ReceivedMessageNI.getInstance(this));
		receivedThread.start();
	}

	public void checkLocalUsername(String localUsername) {
		if ((localUsername == null) || (localUsername.length() <= 0))
			System.exit(-1);
		try {
			localUsername += "@"+InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void getTalk(int index) {
		messageController.getTalk(index);
	}

	public ChatModel getModel() {
		return chatModel;
	}
}
