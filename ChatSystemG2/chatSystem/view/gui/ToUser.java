package chatSystem.view.gui;

import java.util.List;


public interface ToUser {
	
	public void Deconnected();
	
	public void DisplayFileTransfertProgression();

	public void DisplaySuggestion();
	
	public void DisplayDialogWindow (String contact);
	
	public void DisplayDeclinedSuggestionNotification();
	
	public void DisplayNewMessageNotification();
	
	public void DisplayReceivedFile();
	
	public void DisplayFileSendedNotification();
	
	public void listUser(List<String> newList) throws GUIException;
	
	public void DisplayMessage();
	
	public void DisplayBrowseWindow();
	
	public void DisplayAcceptedSuggestionNotification();
}
