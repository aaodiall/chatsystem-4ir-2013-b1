package org.insa.java.model;

import java.net.InetAddress;
import java.util.ArrayList;

import chatSystemCommon.Message;

/**
 * User entity. Contains informations about user.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public class User {
	private String username;
	private InetAddress address;
	private ArrayList<Message> messageList = new ArrayList<Message>();
	
	/**
	 * Constructor
	 * @param address IP address associated to the user.
	 * @param username Name associated to the user.
	 */
	public User(InetAddress address,String username) {
		this.username = username;
		this.address = address;
	}

	/**
	 * Add a message to the message list of the user.
	 * @param m Message to add.
	 */
	public void addMessage(Message m) {
		this.messageList.add(m);
	}
	
	/**
	 * Remove a message from the message list of the user.
	 * @param m Message to remove.
	 */
	public void removeMessageList(Message m) {
		this.messageList.remove(m);
	}
	
	/**
	 * Get user name.
	 * @return name User name.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set user name.
	 * @param username New user name.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get user address.
	 * @return address User address.
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Set user address.
	 * @param address New user address.
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
	/**
	 * Get message list.
	 * @return messageList The list of messages.
	 */
	public ArrayList<Message> getMessageList() {
		return messageList;
	}

	/**
	 * User description method.
	 * @return username User name.
	 */
	@Override
	public String toString() {
		return username;
	}

	/**
	 * User compare method.
	 * @param o User to compare with.
	 * @return true if the two users are the same, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		User u = (User) o;
		if(!address.equals(u.getAddress()))
			return false;
		if(!username.equals(u.getUsername()))
			return false;
		return true;
	}
}
