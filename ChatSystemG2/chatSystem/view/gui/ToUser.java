/**
 * Represents all the messages that can be sent to the local user
 */

package chatSystem.view.gui;

import chatSystem.model.FileReceivingInformation;
import chatSystem.model.FileTransfertInformation;
import java.util.List;


public interface ToUser {
	
	public void disconnected();
        
        public void connected();
        
        public void displayConnectionErrorNotification();
        
        public void displayFileTransfertErrorNotification(String idRemoteSystem, String fileName);
        
        public void displayFileTransfertProgression(FileTransfertInformation tmp);
	
	public void resetFileTransfertProgression(FileTransfertInformation tmp);

	public void displayFileSuggestion(FileReceivingInformation tmp);
	
	public void displayDialogWindow (String contact);
	
	public void listUser(List<String> newList) throws GUIException;
	
	public void displayMessage(String idRemoteSystem, String newMessage);
}
