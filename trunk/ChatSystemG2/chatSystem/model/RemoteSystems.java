/**
 * Keep and manage the remote systems' information
 * This class implements the Singleton's pattern
 */

package chatSystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RemoteSystems extends Model implements Iterable<RemoteSystemInformation> {

    private static RemoteSystems instance;
    private final Map<String, RemoteSystemInformation> remoteSystemsInformation;

    /**
     * Private class' constructor
     */
    private RemoteSystems() {
        this.remoteSystemsInformation = new HashMap<String, RemoteSystemInformation>();
    }

    /**
     * Add a remote system in the remote system's list
     * @param username contact's username
     * @param ip remote system's ip address
     * @param isAck whether or not the remote system expects an answer
     */
    public synchronized void addRemoteSystem(String username, String ip, boolean isAck) {
        
        String key = RemoteSystemInformation.generateID(username, ip);
        if (!this.remoteSystemsInformation.containsKey(key)) {
            System.out.println("Adding a user : " + username + "     " + ip);
            RemoteSystemInformation newRS = new RemoteSystemInformation(username, ip);
            this.remoteSystemsInformation.put(key, newRS);
            this.setChanged();
            this.notifyObservers(ip);
            this.clearChanged();
        } else {
            RemoteSystemInformation aux = this.getRemoteSystem(key);
            if (aux.getUserState() == UserState.CONNECTED && !isAck) {
                //message sent by a remote system to indicate it's still connected
                aux.setUserState(UserState.CONNECTED);
                this.setChanged();
                this.notifyObservers(ip);
                this.clearChanged();
        
            }else if (aux.getUserState() == UserState.MAYBEOFFLINE) {
                //message sent to answer a hello broadcast to determine which remote system is still connected
                aux.setUserState(UserState.CONNECTED);
            }
        }
    }

    /**
     * Remove a remote system from the list
     * @param idRemoteSystem id of the remote system to be removed
     */
    public synchronized void deleteRemoteSystem(String idRemoteSystem) {
        this.remoteSystemsInformation.remove(idRemoteSystem);
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    }

    /**
     * Obtain the information about a given remote system
     * @param idRemoteSystem id of the remote system which is wanted
     * @return instance of RemoteSystemInformation corresponding to the id
     */
    public RemoteSystemInformation getRemoteSystem(String idRemoteSystem) { //supprimer pour faire des liens comme la fonctionen dessous
        return this.remoteSystemsInformation.get(idRemoteSystem);
    }

    /**
     * Add a received message in a given remote system's list
     * @param idRemoteSystem id of the remote system
     * @param message message which is to be added
     */
    public synchronized void addMessageReceivedToRemote(String idRemoteSystem, String message) {
        this.remoteSystemsInformation.get(idRemoteSystem).addMessageReceived(message);
    }

    /**
     * add a message in the message-to-send list of a given remote system
     * @param idRemoteSystem remote system the message has to be sent to
     * @param message message which has to be sent
     */
    public synchronized void addMessageToSendToRemote(String idRemoteSystem, String message) {
        if (this.remoteSystemsInformation.containsKey(idRemoteSystem)) {
            this.remoteSystemsInformation.get(idRemoteSystem).addMessageToSend(message);
            this.setChanged();
            this.notifyObservers(this.remoteSystemsInformation.get(idRemoteSystem));
            this.clearChanged();
        }
    }

    /**
     * add a message in the sent-message list of a given remote system
     * @param message message that has been sent
     * @param idRemoteSystem remote system the message has been sent to
     */
    public void addMessageSentToRemoteSystem(String message, String idRemoteSystem) {
        if (this.remoteSystemsInformation.containsKey(idRemoteSystem)) {
            this.remoteSystemsInformation.get(idRemoteSystem).addMessageSent(message);
        }
    }

    /**
     * Get the contacts username's list
     * @return usernames' list
     */
    public List<String> getUserList() {
        List<String> userList = new ArrayList<String>();
        for (String rs : this.remoteSystemsInformation.keySet()) {
            userList.add(this.remoteSystemsInformation.get(rs).getIdRemoteSystem());
        }
        return userList;
    }

    /**
     * Set all the remote systems' state as maybeoffline
     * Will launch a search for the remote systems which are still connected
     * And those who are not
     */
    public void setAllMaybeOffline() {
        for (RemoteSystemInformation rsi: this) {
            rsi.setUserState(UserState.MAYBEOFFLINE);
        }
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    }
    
    /**
     * Remove from the list the remote systems which appeared to be offline
     */
    public void removeOfflineRemoteSystem() {
        for (RemoteSystemInformation rsi: this) {
            if (rsi.getUserState() == UserState.MAYBEOFFLINE)
                //rsi.setUserState(UserState.DISCONNECTED);
                this.remoteSystemsInformation.remove(rsi.getIdRemoteSystem());
        }
    }
    
    /**
     * Static method to obtain an instance of the class RemoteSystems
     * @return RemoteSystems unique instance
     */
    public final static RemoteSystems getInstance() {
        if (instance == null) {
            synchronized (RemoteSystems.class) {
                if (instance == null) {
                    instance = new RemoteSystems();
                }
            }
        }
        return instance;
    }

    /**
     * Provide an iterator on the instance
     * The aim is for others objects to be able to look through the instance 
     * without knowing from what it is composed
     * @return instance's interator
     */
    @Override
    public Iterator<RemoteSystemInformation> iterator() {
        return new RSIterator();
    }

    private class RSIterator implements Iterator<RemoteSystemInformation> {

        //private Set<RemoteSystemInformation> info;
        private Iterator<String> it;

        public RSIterator() {
            this.it = remoteSystemsInformation.keySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public RemoteSystemInformation next() {
            return remoteSystemsInformation.get(it.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
