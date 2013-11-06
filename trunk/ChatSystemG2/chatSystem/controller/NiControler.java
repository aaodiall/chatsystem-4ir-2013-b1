package chatSystem.controller;

public interface NiControler {
	
	public void performHelloReceived(boolean ack);

	public void GoodbyeReceived(String idRemoteSystem);
	
	public void performMessageReceived();
	
	public void performSendMessageRequest(String message);
	
	public void performSendFileRequest();
	
	public void performSuggestionReceived(FileTransfertDemand suggestion);
	
	public void performAcceptSuggestion();
	
	public void performDeclinedSuggestion();
	
	public void performFileReceived(File f);
}
