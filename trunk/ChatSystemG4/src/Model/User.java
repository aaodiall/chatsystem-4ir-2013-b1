package Model;

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

	public void setTalkList(ArrayList<Message> messageList) {
		this.messageList = messageList;
	}

	@Override
	public String toString() {
		return username;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
}
