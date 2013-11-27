/**
 * Keep and manage all the information about a remote system (Username,
 * address, id, messages)
 */

package chatSystem.model;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Vector;


public class RemoteSystemInformation extends UserInformation {

    //faire une seule liste et faire unJlist ds la fenetre pour afficher directement la liste
    private final ConcurrentLinkedQueue<String> messagesToSend;
    private final Vector<String> messages; //passer à autre chose et gérer tout dans le update comme pour la liste utilisateur

    /**
     * Class' constructor
     * @param username contact's username
     * @param ip remote system's ip address
     */
    public RemoteSystemInformation(String username, String ip) {
        super(username, ip);
        this.messagesToSend = new ConcurrentLinkedQueue<String>();
        this.messages = new Vector<String>(1000);
    }

    /**
     * Add a message in the received messages' list
     * @param message new received message
     */
    public synchronized void addMessageReceived(String message) {
        this.messages.add(message);
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
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
        return this.messagesToSend.poll();
    }

    /**
     * Add a message in the sent messages' list
     * @param message sent message
     */
    public synchronized void addMessageSent(String message) {
        this.messages.add(message); 
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    }
    

    /**
     * Get the conversation between the user and this remote system
     * @return list of all the exchanges messages
     */
    public List<String> getMessages() {
        int sizeM = this.messages.size();
        if (sizeM <= 100)
            return this.messages;
        else 
            return this.messages.subList(sizeM-101, sizeM-1);
    }

    /**
     * Get the remote system's unique id Obtained by joining the ip address and
     * the contact's username
     * @return remote system's id
     */
    public String getIdRemoteSystem() {
        return RemoteSystemInformation.generateID(this.getUsername(),this.getIP());
    }
    
    /**
     * Set the remote system's state (connected/disconnected/maybeoffline)
     * @param state remote system's state
     */
    @Override
    public void setUserState(UserState state) {
            this.state = state;
    }
 
    /**
     * Static method using to generate a remote system's id
     * @param username username of the contact using this remote system
     * @param ip ip adress of the remote system
     * @return remote system's id
     */
    public static String generateID(String username, String ip) {
        return username + "@" + ip;
    }
}
