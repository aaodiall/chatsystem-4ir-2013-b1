package org.insa.java.view;

import java.io.File;

import org.insa.java.controller.ChatController;
import org.insa.java.model.User;

/**
 * Generic graphic interface for java-based application.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public abstract class JavaChatGUI implements FromUser, ToUser{
	protected ChatController chatController;
	protected JavaStatusBar statusBar;

	@Override
	public void sendTextMessage(User selectedUser, String text) {
		if(selectedUser != null && !text.isEmpty())
			chatController.sendTextMessage(selectedUser, text);
	}

	@Override
	public void sendFile(User user, File file) {
		chatController.sendFile(user, file);
	}

	@Override
	public void connect() {
		chatController.connect();
	}

	@Override
	public void disconnect() {
		chatController.disconnect();
		chatController.sendGoodbyeMessage(chatController.getModel().getLocalUser());
	}

	@Override
	public void displayMessageInformation(String s) {
		statusBar.messageBar.setText(s);
	}

	@Override
	public void displayReceptionTransferInformation() {
		statusBar.receptionBar.reset();
		statusBar.receptionBar.setText("File reception processing...");
		statusBar.receptionBar.setVisible(true);
		statusBar.receptionBar.setMax(100);
	}
	
	@Override
	public void hideReceptionTransferInformation() {
		statusBar.receptionBar.setVisible(false);
		statusBar.receptionBar.setText("File emission terminated");
		statusBar.receptionBar.reset();
	}
	
	@Override
	public void displayReceptionTransferPercent(int percent) {
		statusBar.receptionBar.setValue(percent);
	}

	@Override
	public void displayEmissionTransferInformation() {
		statusBar.emissionBar.reset();
		statusBar.emissionBar.setText("File reception processing...");
		statusBar.emissionBar.setVisible(true);
		statusBar.emissionBar.setMax(100);
	}
	
	@Override
	public void hideEmissionTransferInformation() {
		statusBar.emissionBar.setVisible(false);
		statusBar.emissionBar.setText("File emission terminated");
		statusBar.emissionBar.reset();
	}
	
	@Override
	public void displayEmissionTransferPercent(int percent) {
		statusBar.emissionBar.setValue(percent);
	}
	
	@Override
	public void displayEmissionTransferError(String s) {
		statusBar.emissionBar.setText(s);
	}
	
	@Override
	public void displayReceptionTransferError(String s) {
		statusBar.emissionBar.setText(s);
	}
	
	@Override
	public void cancelEmissionTransfer() {
		chatController.cancelEmissionTransfer();
	}
	
	@Override
	public void cancelReceptionTransfer() {
		chatController.cancelReceptionTransfer();
	}
	
	@Override
	public abstract String displayUsernameInputDialog();

	@Override
	public abstract void displayTalk(String talk);

	@Override
	public abstract String getFilePath();
	
	/**
	 * Get the index of the selected user.
	 * @return index Index of the selected user.
	 */
	public abstract int getSelectedUserIndex();

	/**
	 * Get the main frame of the application.
	 * @return frame. The main frame of the application.
	 */
	public abstract Object getFrame();
	
	/**
	 * Get status barfrom the application.
	 * @return statusBar Status bar.
	 */
	public JavaStatusBar getStatusBar() {
		return statusBar;
	}
}
