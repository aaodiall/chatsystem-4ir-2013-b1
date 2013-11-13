package chatSystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps and manages all the information about a remote system
 * (Username, address, id, messages)
 * @author Marjorie
 */
public class RemoteSystemInformation extends UserInformation {
	
	List<String> messagesReceived;
	List<String> messagesToSend;
	List<String> messagesSent;
	
        /**
         * Class' constructor
         * @param ip remote system's ip address
         */
	public RemoteSystemInformation (String ip) {
		super(ip);
		this.messagesReceived = new ArrayList<> ();
		this.messagesToSend = new ArrayList<> ();
		this.messagesSent = new ArrayList<> ();
	}
	
        /**
         * Class' constructor
         * @param username contact's username
         * @param ip remote system's ip address
         */
        public RemoteSystemInformation(String username, String ip) {
            super(username,ip);
            this.messagesReceived = new ArrayList<> ();
            this.messagesToSend = new ArrayList<> ();
            this.messagesSent = new ArrayList<> ();
        }
        
        /**
         * Add a message in the received messages' list
         * @param message new received message
         */
	public void addMessageReceived(String message) {
		this.messagesReceived.add(message);
	}
	
        /**
         * Get the newly received message
         * @return received message 
         */
	public String getMessageReceived() {
		return "";
	}
	
        /**
         * Add a message in the to-send messages' list
         * @param message message to send
         */
	public void addMessageToSend(String message) {
		this.messagesToSend.add(message);
	}
	
        /**
         * Get the message which is to be sent
         * @return message to send
         */
	public String getMessageToSend() {
		return "";
	}
	
        /**
         * Add a message in the sent messages' list
         * @param message sent message
         */
	public void addMessageSent(String message) {
		this.messagesSent.add(message);
	}
	
        /**
         * Get the newly sent message
         * @return sent message
         */
	public String getMessageSent() {
		return "";
	}
	
        /**
         * Get the remote system's unique id
         * Obtained by joining the ip address and the contact's username
         * @return remote system's id
         */
	/*public String getIdRemoteSystem() {
		return this.getUsername()+this.getIP();
	}*/

}
