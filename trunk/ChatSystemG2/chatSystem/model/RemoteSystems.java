package chatSystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Keeps and manages the remote system's information
 * @author Marjorie
 */
public class RemoteSystems extends Model{

    private static RemoteSystems instance;
    private final Map<String,RemoteSystemInformation> remoteSystemsInformation;

    /**
     * Private class' constructor
     */
    private RemoteSystems () {
            this.remoteSystemsInformation = new HashMap<String, RemoteSystemInformation>();
    }

    /**
     * Add a remote system in the remote system's list
     * @param username contact's username
     * @param ip remote system's ip address
     */
    public synchronized void addRemoteSystem(String username, String ip) {
        RemoteSystemInformation newRS = new RemoteSystemInformation(username,ip);
        String key = newRS.getUsername();//newRS.getIdRemoteSystem();
        if (!this.remoteSystemsInformation.containsKey(key)) {
            this.remoteSystemsInformation.put(key, newRS);
            this.setChanged();
            //this.notifyObservers(new ArrayList<String>(this.remoteSystemsInformation.keySet()));
            //Quand on ajoute un Remote System on passe en argument l'ip pour que ChatNI réponde Hello
            //ChatGUI lui fait tjrs getUserList
            this.notifyObservers(ip);
            this.clearChanged();
        }
    }

    /**
     * Remove a remote system from the list
     * @param idRemoteSystem id of the remote system to be removed
     */
    public synchronized void deleteRemoteSystem(String idRemoteSystem) {
        this.remoteSystemsInformation.remove(idRemoteSystem);
        this.setChanged();
        //this.notifyObservers(new ArrayList<String>(this.remoteSystemsInformation.keySet()));
        //Quand on enleve un Remote System on passe rien car ChatNI n'a rien a faire
        //ChatGUI lui fait tjrs getUserList
        this.notifyObservers();
        this.clearChanged();
    }

    /**
     * Get the contacts username's list
     * @return usernames' list
     */
    public List<String> getUserList() {
            List<String> userList = new ArrayList<>();
            for(String rs: this.remoteSystemsInformation.keySet()) {
                    userList.add(this.remoteSystemsInformation.get(rs).getUsername());
            }
            return userList;
    }

    /**
     * Static method to obtain an instance of the class RemoteSystems
     * @return RemoteSystems unique instance
     */
    public final static RemoteSystems getInstance() {
            if (instance == null) {
                synchronized(RemoteSystems.class) {
                    if (instance == null) {
                        instance = new RemoteSystems();
                    }
                }
            }
            return instance;
    }
}
