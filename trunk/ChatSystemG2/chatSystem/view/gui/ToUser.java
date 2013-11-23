package chatSystem.view.gui;

import chatSystem.model.FileReceivingInformation;
import chatSystem.model.FileSendingInformation;
import java.util.List;


public interface ToUser {
	
	public void disconnected();
        
        public void connected();
	
	public void displayFileTransfertProgression();

	public void displaySuggestion(FileReceivingInformation tmp);
	
	public void displayDialogWindow (String contact);
	
	public void displayDeclinedSuggestionNotification(FileSendingInformation tmp);
	
	//public void displayNewMessageNotification();
	
	public void displayFileReceivedNotification(FileReceivingInformation tmp);
	
	public void displayFileSendedNotification(FileSendingInformation tmp);
	
	public void listUser(List<String> newList) throws GUIException;
	
	//public void displayMessage();
	
	public void displayAcceptedSuggestionNotification(FileSendingInformation tmp);
}
