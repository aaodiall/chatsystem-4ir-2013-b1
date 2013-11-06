package chatSystem.view.gui;

/** Actions the chat system's user can start **/

public interface FromUser {

	public void Connect();
	
	public void Username(String pseudo);
	
	public void Disconnect();
	
	public void SendMessageRequest(String message);
	
	public void SelectDirectory(String directory);
	
	public void SaveFile();
	
	public void OpenDialogWindow();

	public void AcceptSuggestion();
	
	public void ConfirmSending();
	
	public void SendFileRequest();
	
	public void SelectFile();
	
	public void DeclineSuggestion();
}
