package org.insa.java.view;

import java.io.File;

import org.insa.java.model.User;

public interface FromUser {
	
	public void connect();
	
	public void disconnect();
	
	public void sendTextMessage(User selectedUser, String text);
	
	public void sendFile(User user, File file);
	
	public String getFilePath();
	
	public void cancelEmissionTransfer();
	
	public void cancelReceptionTransfer();
}
