package chatSystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Keeps and manages the remote system's information
 *
 * @author Marjorie
 */
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
     *
     * @param username contact's username
     * @param ip remote system's ip address
     */
    public synchronized void addRemoteSystem(String username, String ip) {
        System.out.println("Ajout utilisateur : " + username + "     " + ip);
        RemoteSystemInformation newRS = new RemoteSystemInformation(username, ip);
        String key = newRS.getIdRemoteSystem();
        if (!this.remoteSystemsInformation.containsKey(key)) {
            this.remoteSystemsInformation.put(key, newRS);

            this.setChanged();
            this.notifyObservers(ip);
            this.clearChanged();
        }
    }

    /**
     * Remove a remote system from the list
     *
     * @param idRemoteSystem id of the remote system to be removed
     */
    public synchronized void deleteRemoteSystem(String idRemoteSystem) {
        this.remoteSystemsInformation.remove(idRemoteSystem);

        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    }

    public RemoteSystemInformation getRemoteSystem(String idRemoteSystem) {
        return this.remoteSystemsInformation.get(idRemoteSystem);
    }

    public synchronized void addMessageReceivedToRemote(String idRemoteSystem, String message) {
        this.remoteSystemsInformation.get(idRemoteSystem).addMessageReceived(message);
    }

    /**
     * add a message in the message-to-send list of a given remote system
     *
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
     *
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
     * Static method to obtain an instance of the class RemoteSystems
     *
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
