package org.insa.java.model;

import java.net.InetAddress;
import java.util.ArrayList;

import chatSystemCommon.Message;

public class User {
	private String username;
	private InetAddress address;
	private ArrayList<Message> messageList = new ArrayList<Message>();
	
	public User(InetAddress address,String username) {
		this.username = username;
		this.address = address;
	}

	public void addMessage(Message m) {
		this.messageList.add(m);
	}
	
	public void removeMessageList(Message m) {
		this.messageList.remove(m);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public ArrayList<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(ArrayList<Message> messageList) {
		this.messageList = messageList;
	}

	@Override
	public String toString() {
		return username.split("@")[0];
	}

	@Override
	public boolean equals(Object o) {
		User u = (User) o;
		//if(!address.equals(u.getAddress()))
		//	return false;
		if(!username.equals(u.getUsername()))
			return false;
		return true;
	}
}
