package org.insa.controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

import org.insa.general.view.GeneralChatGUI;
import org.insa.java.view.ReceivedMessageNI;
import org.insa.java.view.SendMessageNI;
import org.insa.model.ChatModel;
import org.insa.model.User;
import org.java.factory.MessageFactory;

import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;

import com.sun.istack.internal.logging.Logger;

public class ChatController {
	private ChatModel chatModel;
	private GeneralChatGUI chatGUI;
	private MessageController messageController;
	private FileController fileController;
	private Thread receivedThread;
	
	
	public ChatController(GeneralChatGUI chatGUI) {
		this.chatModel = null;
		this.chatGUI = chatGUI;
		this.messageController = new MessageController(this);
		this.fileController = new FileController(this);
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
		} catch (UnknownHostException e) {
			Logger.getLogger(GeneralChatGUI.class).log(Level.SEVERE, null, e);
		}
		this.sendHelloMessage(this.getLocalUser());	
		this.enableMessageReception();
		
	}
	
	public void enableMessageReception() {
		receivedThread = new Thread(ReceivedMessageNI.getInstance(this));
		receivedThread.start();
	}
	
	public void disableMessageReception() {
		receivedThread = null;
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
	
	public User getLocalUser() {
		return chatModel.getLocalUser();
	}
	
	public void getTalk(int index) {
		User u = chatModel.get(index);
		String talk = "";
		
		for(Message m : u.getMessageList()) {
			if(m instanceof Text)
				talk += m.getUsername() + " : " + ((Text) m).getText() + "\n";
		}
		
		chatGUI.setChatText(talk);
	}
	
	public void addUser(User user) {
		chatModel.addUser(user);
	}
	
	public void removeUser(User user) {
		chatModel.removeElement(user);
	}
	
	public String getLocalUsername() {
		return this.getLocalUser().getUsername();
	}

	public void addMessage(User user, Message msg) {
		chatModel.get(user).addMessage(msg);
	}
	
	public int getSelectedIndex() {
		return chatGUI.getSelectedIndex();
	}
	
	public User getUser(int index) {
		return chatModel.get(index);
	}

	public void setLocalUser(String localUsername) {
		chatModel.setLocalUser(new User(null,localUsername));
	}

	public void updateFileTransferProgress(long l) {
		chatGUI.showFileTransferProgress("File transfer processing (" + l + "%)", (int) l);
	}
	
	public ChatModel getModel() {
		return chatModel;
	}

	public GeneralChatGUI getChatGUI() {
		return chatGUI;
	}
}
