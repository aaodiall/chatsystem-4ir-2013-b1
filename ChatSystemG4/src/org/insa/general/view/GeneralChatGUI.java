package org.insa.general.view;

import java.io.File;

import org.insa.controller.ChatController;
import org.insa.model.User;


public abstract class GeneralChatGUI {
	protected ChatController chatController;
	protected GeneralStatusBar statusBar;
		
	public void sendTextMessage(User selectedUser, String text) {
		if(selectedUser != null)
			chatController.sendTextMessage(selectedUser, text);
	}

	public void sendFile(User user, File file) {
		chatController.sendFile(user, file);
	}
	
	public void connect() {
		chatController.connect();
	}
	
	public void disconnect() {
		chatController.sendGoodbyeMessage(chatController.getLocalUser());
	}
	
	public abstract String usernameInput();

	public abstract void setChatText(String talk);
	
	public abstract int getSelectedIndex();
	
	public abstract String getFilePath();
	
	public GeneralStatusBar getStatusBar() {
		return statusBar;
	}
}
