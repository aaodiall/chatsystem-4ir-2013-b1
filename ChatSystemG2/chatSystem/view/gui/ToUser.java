package chatSystem.view.gui;

import chatSystem.model.FileReceivingInformation;
import java.util.List;


public interface ToUser {
	
	public void disconnected();
        
        public void connected();
	
	public void displayFileTransfertProgression();

	public void displaySuggestion(FileReceivingInformation tmp);
	
	public void displayDialogWindow (String contact);
	
	public void displayDeclinedSuggestionNotification();
	
	public void displayNewMessageNotification();
	
	public void displayReceivedFile();
	
	public void displayFileSendedNotification();
	
	public void listUser(List<String> newList) throws GUIException;
	
	public void displayMessage();
	
	public void displayAcceptedSuggestionNotification();
}
