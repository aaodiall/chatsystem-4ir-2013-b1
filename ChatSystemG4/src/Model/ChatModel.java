package Model;

import javax.swing.DefaultListModel;

@SuppressWarnings("serial")
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
