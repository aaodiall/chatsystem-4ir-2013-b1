package chatSystem.view.gui;

import java.io.File;

/** Actions the chat system's user can start **/

public interface FromUser {

	public void connect(String username);
	
	public void disconnect();
	
	public void sendMessageRequest(String message, String idRemoteSystem);
	
	public void selectDirectory(String directory);
	
	public void saveFile();
	
	public void openDialogWindow();

	public void acceptSuggestion(int idTransfert);
	
	public void confirmSending();
	
	public void sendFileRequest(File fileToSend, String idRemoteSystem);
	
	public void selectFile();
	
	public void declineSuggestion(int idTransfert);
}
