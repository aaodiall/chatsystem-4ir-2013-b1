package org.insa.java.view;

import java.io.File;

import org.insa.java.controller.ChatController;
import org.insa.java.model.User;

public abstract class JavaChatGUI {
	protected ChatController chatController;
	protected JavaStatusBar statusBar;

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
		chatController.sendGoodbyeMessage(chatController.getModel().getLocalUser());
	}

	public abstract String usernameInput();

	public abstract void setChatText(String talk);

	public abstract int getSelectedIndex();

	public abstract String getFilePath();

	public abstract Object getFrame();

	public JavaStatusBar getStatusBar() {
		return statusBar;
	}
}
