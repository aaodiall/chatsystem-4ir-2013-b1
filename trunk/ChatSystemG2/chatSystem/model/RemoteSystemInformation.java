package chatSystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Keeps and manages all the information about a remote system (Username,
 * address, id, messages)
 *
 * @author Marjorie
 */
public class RemoteSystemInformation extends UserInformation {

   //faire une seule liste et faire unJlist ds la fenetre pour afficher directement la liste
    
    private final ConcurrentLinkedQueue<String> messagesToSend;
    private final ConcurrentLinkedQueue<String> messages;

    /**
     * Class' constructor
     *
     * @param username contact's username
     * @param ip remote system's ip address
     */
    public RemoteSystemInformation(String username, String ip) {
        super(username, ip);
        
        this.messagesToSend = new ConcurrentLinkedQueue<String>();
        this.messages = new ConcurrentLinkedQueue<String>();
        
    }

    /**
     * Add a message in the received messages' list
     *
     * @param message new received message
     */
    public void addMessageReceived(String message) {
        this.messages.add(message); //rajouter id devant pr identifier
    }


    /**
     * Add a message in the to-send messages' list
     *
     * @param message message to send
     */
    public void addMessageToSend(String message) {
        this.messagesToSend.add(message); 
    }

    /**
     * Get the message which is to be sent
     *
     * @return message to send
     */
    public ConcurrentLinkedQueue<String> getMessageToSend() {
        return this.messagesToSend;
    }

    /**
     * Add a message in the sent messages' list
     *
     * @param message sent message
     */
    public void addMessageSent(String message) {
        this.messages.add(message); //rajouter username
        
    }

    /**
     * Get the remote system's unique id Obtained by joining the ip address and
     * the contact's username
     *
     * @return remote system's id
     */
    /*public String getIdRemoteSystem() {
     return this.getUsername()+this.getIP();
     }*/
}
