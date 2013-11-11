package Model;

import javax.swing.DefaultListModel;

@SuppressWarnings("serial")
public class ChatModel extends DefaultListModel<User> {
	private User localUser;
	
	public ChatModel(User user) {
		this.localUser = user;
	}
	
	public void addUser(User u) {
		this.addElement(u);
	}
	
	public void removeUser(User u) {
		this.removeElement(u);
	}

	public User getLocalUser() {
		return localUser;
	}

	public void setLocalUser(User localUser) {
		this.localUser = localUser;
	}

	public User get(User u) {
		return this.get(this.indexOf(u));
	}
}
