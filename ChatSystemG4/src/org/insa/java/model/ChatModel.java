package org.insa.java.model;

import javax.swing.DefaultListModel;

/**
 * Model of the chat system. Contains data using by the application.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public class ChatModel extends DefaultListModel<User>{
	private static final long serialVersionUID = -4071263680161746323L;

	private User localUser;
	private User fileTransferClient = null;

	/**
	 * Constructor
	 * @param localUser Local user.
	 */
	public ChatModel(User localUser) {
		this.localUser = localUser;
	}

	/**
	 * Add an user to the list of users.
	 * @param user User to add.
	 */
	public void addUser(User user) {
		this.addElement(user);
	}

	/**
	 * Remove an user from the list of users.
	 * @param user User to remove.
	 */
	public void removeUser(User user) {
		this.removeElement(user);
	}

	/**
	 * Get local user.
	 * @return localUser Local user.
	 */
	public User getLocalUser() {
		return localUser;
	}

	/**
	 * Set localu user.
	 * @param localUser New local user
	 */
	public void setLocalUser(User localUser) {
		this.localUser = localUser;
	}

	/**
	 * Get a specific user from the list.
	 * @param user User to return.
	 * @return user The real user from the list.
	 */
	public User get(User user) {
		return this.get(this.indexOf(user));
	}
	
	/**
	 * Get the file transfer client.
	 * @return fileTransferClient Last file transfer client.
	 */
	public User getFileTransferClient() {
		return fileTransferClient;
	}

	/**
	 * Set the file transfer client.
	 * @param fileTransferClient New file transfer client.
	 */
	public void setFileTransferClient(User fileTransferClient) {
		this.fileTransferClient = fileTransferClient;
	}
	
	/**
	 * Get local user name.
	 * @return name The name of the local user.
	 */
	public String getLocalUsername() {
		return localUser.getUsername();
	}

	
}
