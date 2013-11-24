package chatSystem.view.gui;

import chatSystem.model.FileReceivingInformation;
import chatSystem.model.FileSendingInformation;
import chatSystem.model.FileTransfertInformation;
import java.util.List;


public interface ToUser {
	
	public void disconnected();
        
        public void connected();
	
	public void displayFileTransfertProgression(FileTransfertInformation tmp);

	public void displayFileSuggestion(FileReceivingInformation tmp);
	
	public void displayDialogWindow (String contact);
	
	//public void displayNewMessageNotification();
	
	public void listUser(List<String> newList) throws GUIException;
	
	//public void displayMessage();
}
