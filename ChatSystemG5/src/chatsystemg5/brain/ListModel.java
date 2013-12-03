package chatsystemg5.brain;

import chatsystemg5.ihm.ListWindow;
import java.util.HashMap;
import java.util.Observer;

/**
 *
 */
public class ListModel extends ChatModel {
    
    private ListWindow observer_list;

    private HashMap<String,String> hmap_users;
    private ChatController chat_control;
    
    // Temporary list of remote users
    private HashMap<String,String> hmap_temp;
    
    /**
     *
     * @param chat_control
     */
    public ListModel (ChatController chat_control) {
        this.chat_control = chat_control;
        hmap_users = new HashMap();
    }
    
    /**
     *
     * @param username
     * @param IP_addr
     */
    public void add_user (String username, String IP_addr) {
        if (!this.hmap_users.containsKey(username + "@" + IP_addr)) {
            hmap_users.put (username + "@" + IP_addr, IP_addr);
        }
        this.notifyObservers();
    }
    
    
    
    /**
     *
     * @param username
     * @param IP_addr
     */
    public void add_user_temp (String username, String IP_addr) {
        if (!this.hmap_temp.containsKey(username + "@" + IP_addr)) {
            hmap_temp.put (username + "@" + IP_addr, IP_addr);
        }
    }
    
    /**
     *
     */
    public void new_temp() {
        hmap_temp = new HashMap();
    }
    
    
    
    /**
     *
     * @param username
     * @param IP_addr
     */
    public void remove_user (String username, String IP_addr) {
        if(hmap_users.containsKey(username + "@" + IP_addr)){
            hmap_users.remove(username + "@" + IP_addr);
            this.notifyObservers();
        }
    }
    
    /**
     *
     * @param full_name
     */
    public void remove_from_full_name (String full_name) {
        hmap_users.remove(full_name);
        this.notifyObservers();
    }
    
    /**
     *
     * @param username
     * @return
     */
    public String get_IP_addr (String username) {
        return hmap_users.get(username);
    }
    
    /**
     *
     * @return
     */
    public HashMap<String,String> get_hmap_users () {
        return this.hmap_users;
    }
    
    /**
     *
     * @return
     */
    public HashMap<String,String> get_hmap_temp () {
        return this.hmap_temp;
    }
    
    @Override
    public void notifyObservers(){
        this.observer_list.update(this, this.hmap_users);
    }
    
    @Override
    public void addObserver(Observer o){ 
        this.observer_list = (ListWindow)o;
    }
    
    @Override
    public void deleteObservers(){
        this.observer_list.dispose();
    }

    /**
     *
     * @param o
     */
    public void set_hmap_users(HashMap<String, String> o) {
        this.hmap_users = o;
    }
}
