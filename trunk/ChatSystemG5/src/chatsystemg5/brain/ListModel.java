package chatsystemg5.brain;

import chatsystemg5.ihm.ListWindow;
import java.util.HashMap;
import java.util.Observer;

/**
 * Represents the ListModel<p>
 * Handles the user list<p>
 * 
 * Implements Observable to notify the ListWindow when change occurs<p>
 * 
 * observer_list : references the unique ListWindow observer<p>
 * hmap_users : references a hashmap with "username@IP" as keys and "IP" as values<p>
 * chat_control : references the unique ChatController<p>
 * hmap_temp (temporary) : references the futur hmap_users, it is used to check remote user connection
 */
public class ListModel extends ChatModel {
    
    private ChatController chat_control;
    
    private ListWindow observer_list;

    private HashMap<String,String> hmap_users;
    private HashMap<String,String> hmap_temp;
    
    /**
     * Links the ListModel to the unique controller<p>
     * Initializes a new users collection<p>
     * 
     * @param chat_control references the unique ChatController
     */
    public ListModel (ChatController chat_control) {
        this.chat_control = chat_control;
        this.hmap_users = new HashMap();
    }
    
    /**
     * Adds a remote user and his IP to the ListModel<p>
     * "username@IP" is the key, "IP" is the value<p>
     * Checks if the key already exists in the list<p>
     * If it exists then nothing happens<p>
     * Else the remote user is added to the list and the observer is notified
     * 
     * @param username references the remote user username
     * @param IP_address reference the remote user IP
     */
    public void add_user (String username, String IP_address) {
        if (!this.hmap_users.containsKey(username + "@" + IP_address)) {
            this.hmap_users.put (username + "@" + IP_address, IP_address);
            this.notifyObservers();
        }
    }
    
    
    
    /**
     * Adds a remote user and his IP to the ListModel in a temporary list<p>
     * "username@IP" is the key, "IP" is the value<p>
     * Checks if the key already exists in the list<p>
     * If it exists then nothing happens<p>
     * Else the remote user is added to the list
     * 
     * @param username references the remote user username
     * @param IP_address reference the remote user IP
     */
    public void add_user_temp (String username, String IP_address) {
        if (!this.hmap_temp.containsKey(username + "@" + IP_address)) {
            this.hmap_temp.put (username + "@" + IP_address, IP_address);
        }
    }
    
    /**
     * Initializes a new temporary list
     */
    public void new_temp() {
        this.hmap_temp = new HashMap();
    }
    
    
    
    /**
     * Removes a user from the list<p>
     * Checks if the user exists or not in the list<p>
     * If it exists then it's removed and the obersver is notified<p>
     * Else nothing happens
     * 
     * @param username references the remote user username
     * @param IP_address reference the remote user IP
     */
    public void remove_user (String username, String IP_address) {
        if(this.hmap_users.containsKey(username + "@" + IP_address)){
            this.hmap_users.remove(username + "@" + IP_address);
            this.notifyObservers();
        }
    }
    
    /**
     * Allows to get the the IP address of a remote user
     * @param id references the "username@ip" key of the hmap_users
     * @return the IP address of a remote user
     */
    public String get_IP_addr (String id) {
        return hmap_users.get(id);
    }
    
    /**
     * Allows to get the collection of users
     * @return the collection of users
     */
    public HashMap<String,String> get_hmap_users () {
        return this.hmap_users;
    }
    
    /**
     * Allow to get the temporary collection of users
     * @return the temporary collection of users
     */
    public HashMap<String,String> get_hmap_temp () {
        return this.hmap_temp;
    }
    
    /**
     * Calls the update method of the observer (the ListWindow)<p>
     */
    @Override
    public void notifyObservers(){
        this.observer_list.update(this, this.hmap_users);
    }
    
    /**
     * Adds the ListWindow to the obersver
     * @param o the ListWindow
     */
    @Override
    public void addObserver(Observer o){ 
        this.observer_list = (ListWindow)o;
    }
    
    /**
     * Removes the ListWindow of the observer
     */
    @Override
    public void deleteObservers(){
        this.observer_list.dispose();
    }

    /**
     * Allows to replace the used hmap_users by another one
     * Used to refresh the list after checking remote users connection
     * @param o the new list of users to replace the previous
     */
    public void set_hmap_users(HashMap<String, String> o) {
        this.hmap_users = o;
    }
}
