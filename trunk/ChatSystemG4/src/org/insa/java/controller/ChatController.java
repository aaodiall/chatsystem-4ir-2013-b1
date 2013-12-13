package org.insa.java.controller;

import java.io.File;
import java.io.IOException;
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

/**
 * Generic controller.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public class ChatController {
	private ChatModel chatModel;
	private JavaChatGUI chatGUI;
	private MessageController messageController;
	private FileController fileController;
	private Thread receivedThread;	
	
	/**
	 * Constructor
	 * @param chatGUI Main graphic interface.
	 */
	public ChatController(JavaChatGUI chatGUI) {
		this.chatModel = null;
		this.chatGUI = chatGUI;
	}

	/**
	 * Send a text to a destination user.
	 * @param selectedUser Destination user.
	 * @param message Text to send.
	 */
	public void sendTextMessage(User selectedUser, String message) {
		Message text = new Text(chatModel.getLocalUser().getUsername(),message);
		chatModel.get(selectedUser).addMessage(text);
		SendMessageNI.getInstance().sendMessage(text,selectedUser.getAddress());
		this.updateTalk(chatModel.indexOf(selectedUser));	
	}
	
	/**
	 * Send a file to a destination user.
	 * @param user Destination user.
	 * @param file File to send
	 */
	public void sendFile(User user, File file) {
		fileController.beginEmissionProtocol(user, file);
	}

	/**
	 * Send a hello message. A hello message is the first message you have to send went you are connected. It allows you to know who is connected.
	 * @param localUser A reference to local user.
	 */
	public void sendHelloMessage(User localUser) {
		messageController.sendHelloMessage(MessageFactory.hello(chatModel.getLocalUsername(), false));
	}

	/**
	 * Send a bye message. A bye message is the message you have to send when you leave the application.
	 * @param localUser A reference to local user.
	 */
	public void sendGoodbyeMessage(User localUser) {
		messageController.sendGoodbyeMessage(MessageFactory.bye(chatModel.getLocalUsername()));
	}
	
	/**
	 * Generic method to receive messages. Do some verifications and dispatch the received message between message controller and file controller.
	 * @param inetAddress The IP address where the message is coming.
	 * @param msg The received message
	 * @return -1 if error, 0 if ok.
	 */
	public int receivedMessage(InetAddress inetAddress, Message msg){
		if(msg.getUsername().equals(chatModel.getLocalUsername()))
			return -1;
		
		User user = new User(inetAddress,msg.getUsername());
		
		Logger.getLogger(ChatController.class).log(Level.INFO, "Message received >> " + msg.toString());
		
		if(msg instanceof Hello || msg instanceof Goodbye || msg instanceof Text)
			messageController.receivedMessage(user,msg);
		else {
			try {
				fileController.receivedMessage(user,msg);
			} catch (IOException e) {
				Logger.getLogger(ChatController.class).log(Level.SEVERE, "", e);
			}
		}
		return 0;
	}
	
	/**
	 * Connect the localUser.
	 */
	public void connect() {
		String localUsername = chatGUI.displayUsernameInputDialog();
		this.checkLocalUsername(localUsername);
		try {
			chatModel = new ChatModel(new User(InetAddress.getLocalHost(),localUsername));
			this.messageController = new MessageController(this,chatGUI,chatModel);
			this.fileController = new FileController(chatGUI,chatModel);
		} catch (UnknownHostException e) {
			Logger.getLogger(JavaChatGUI.class).log(Level.SEVERE, null, e);
		}
		this.enableMessageReception();
		this.sendHelloMessage(chatModel.getLocalUser());	
	}
	
	/**
	 * Disconnect the localUser.
	 */
	public void disconnect() {
		this.sendGoodbyeMessage(chatModel.getLocalUser());
	}
	
	/**
	 * Enable message reception by creating a new listening thread.
	 */
	public void enableMessageReception() {
		receivedThread = new Thread(ReceivedMessageNI.getInstance(this));
		receivedThread.start();
	}

	/**
	 * Check if the username is correct. Exit the application if no.
	 * @param localUsername Username to be checked
	 */
	private void checkLocalUsername(String localUsername) {
		if ((localUsername == null) || (localUsername.length() <= 0))
			System.exit(-1);
	}
	
	/**
	 * Update the talk of the selected user.
	 * @param index The index of the selected user.
	 */
	public void updateTalk(int index) {
		messageController.updateTalk(index);
	}

	/**
	 * Return the model.
	 * @return ChatModel.
	 */
	public ChatModel getModel() {
		return chatModel;
	}

	/**
	 * Stop emission file transfer.
	 */
	public void cancelEmissionTransfer() {
		fileController.cancelEmissionTransfer();
	}

	/**
	 * Stop reception file transfer.
	 */
	public void cancelReceptionTransfer() {
		fileController.cancelReceptionTransfer();
	}

	/**
	 * Display an error message to the user.
	 * @param s Message to display.
	 */
	public void messageError(String s) {
		chatGUI.showErrorMessage(s);
	}
}
