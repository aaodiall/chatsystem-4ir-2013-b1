package org.insa.java.view;

import java.io.File;

import org.insa.java.controller.ChatController;
import org.insa.java.model.User;

public abstract class JavaChatGUI implements FromUser, ToUser{
	protected ChatController chatController;
	protected JavaStatusBar statusBar;

	public void sendTextMessage(User selectedUser, String text) {
		if(selectedUser != null && !text.isEmpty())
			chatController.sendTextMessage(selectedUser, text);
	}

	public void sendFile(User user, File file) {
		chatController.sendFile(user, file);
	}

	public void connect() {
		chatController.connect();
	}

	public void disconnect() {
		chatController.disconnect();
		chatController.sendGoodbyeMessage(chatController.getModel().getLocalUser());
	}

	public JavaStatusBar getStatusBar() {
		return statusBar;
	}
	
	public void displayMessageInformation(String s) {
		statusBar.messageBar.setText(s);
	}

	public void displayReceptionTransferInformation() {
		statusBar.receptionBar.reset();
		statusBar.receptionBar.setText("File reception processing...");
		statusBar.receptionBar.setVisible(true);
		statusBar.receptionBar.setMax(100);
	}
	
	public void hideReceptionTransferInformation() {
		statusBar.receptionBar.setVisible(false);
		statusBar.receptionBar.setText("File emission terminated");
		statusBar.receptionBar.reset();
	}
	
	public void displayReceptionTransferPercent(int percent) {
		statusBar.receptionBar.setValue(percent);
	}

	public void displayEmissionTransferInformation() {
		statusBar.emissionBar.reset();
		statusBar.emissionBar.setText("File reception processing...");
		statusBar.emissionBar.setVisible(true);
		statusBar.emissionBar.setMax(100);
	}
	
	public void hideEmissionTransferInformation() {
		statusBar.emissionBar.setVisible(false);
		statusBar.emissionBar.setText("File emission terminated");
		statusBar.emissionBar.reset();
	}
	
	public void displayEmissionTransferPercent(int percent) {
		statusBar.emissionBar.setValue(percent);
	}
	
	public void displayEmissionTransferError(String s) {
		statusBar.emissionBar.setText(s);
	}
	
	public void displayReceptionTransferError(String s) {
		statusBar.emissionBar.setText(s);
	}
	
	public void cancelEmissionTransfer() {
		chatController.cancelEmissionTransfer();
	}
	
	public void cancelReceptionTransfer() {
		chatController.cancelReceptionTransfer();
	}
	
	public abstract String displayUsernameInputDialog();

	public abstract void displayTalk(String talk);

	public abstract int getSelectedUserIndex();

	public abstract String getFilePath();

	public abstract Object getFrame();
}
