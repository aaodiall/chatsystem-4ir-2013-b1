package org.insa.java.view;

import java.io.File;

import org.insa.java.model.User;

/**
 * Actions from user to application.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public interface FromUser {
	/**
	 * Connect the localUser.
	 */
	public void connect();
	
	/**
	 * Disconnect the localUser.
	 */
	public void disconnect();
	
	/**
	 * Send a text to a destination user.
	 * @param selectedUser Destination user.
	 * @param message Text to send.
	 */
	public void sendTextMessage(User selectedUser, String text);
	
	/**
	 * Send a file to a destination user.
	 * @param user Destination user.
	 * @param file File to send
	 */
	public void sendFile(User user, File file);
	
	/**
	 * Allows user to selected a place in his computer.
	 * @return path The selected path;
	 */
	public String getFilePath();
	
	/**
	 * Stop emission file transfer.
	 */
	public void cancelEmissionTransfer();
	
	/**
	 * Stop reception file transfer.
	 */
	public void cancelReceptionTransfer();
}
