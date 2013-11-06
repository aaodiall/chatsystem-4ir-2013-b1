package chatSystem.view.ni;


public class ChatNI {
	private MessageReceiver messageReceiver; 
	private MessageTransfert messageTransfert;
	private FileReceiver[] fileReceiver;
	private FileTransfert[] fileTransfert;
	
	public ChatNI() {
		this.fileReceiver = new FileReceiver[5];
		this.fileTransfert = new FileTransfert[5];
	}
	
	
}
