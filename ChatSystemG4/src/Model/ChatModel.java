package Model;

import java.util.ArrayList;
import java.util.Observer;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;

import chatSystemCommon.Message;

public class ChatModel extends DefaultListModel<User> {
	public ChatModel() {
		
	}
	
	public void addUser(User u) {
		this.addElement(u);
	}
	
	
	public void removeUser(User u) {
		this.removeElement(u);
	}
}
