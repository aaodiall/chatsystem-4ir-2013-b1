package chatSystem.model;

import java.util.ArrayList;
import java.util.List;

public class RemoteSystemInformation extends UserInformation {
	
	List<String> messagesReceived;
	List<String> messagesToSend;
	List<String> messagesSent;
	
	public RemoteSystemInformation () {
		super();
		this.messagesReceived = new ArrayList<String> ();
		this.messagesToSend = new ArrayList<String> ();
		this.messagesSent = new ArrayList<String> ();
	}
	
        public RemoteSystemInformation(String username, String ip) {
            super(username,ip);
            this.messagesReceived = new ArrayList<String> ();
            this.messagesToSend = new ArrayList<String> ();
            this.messagesSent = new ArrayList<String> ();
        }
        
	public void addMessageReceived(String message) {
		this.messagesReceived.add(message);
	}
	
	public String getMessageReceived() {
		return "";
	}
	
	public void addMessageToSend(String message) {
		this.messagesToSend.add(message);
	}
	
	public String getMessageToSend() {
		return "";
	}
	
	public void addMessageSent(String message) {
		this.messagesSent.add(message);
	}
	
	public String getMessageSent() {
		return "";
	}
	
	public String getIdRemoteSystem() {
		return this.getUsername()+this.getIP();
	}

}
