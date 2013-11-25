    package chatsystemg5.brain;

import chatsystemg5.ihm.ListWindow;
import java.util.HashMap;
import java.util.Observer;


public class ListModel extends ChatModel {
    
    private ListWindow observer;

    private HashMap<String,String> hmap_users;
    
    public ListModel (ChatController chat_control) {
        hmap_users = new HashMap();
    }
    
    public void add_user(String username, String IP_addr) {
        if (!this.hmap_users.containsKey(username + "@" + IP_addr)) {
            hmap_users.put (username + "@" + IP_addr, IP_addr);
        }
        //System.out.println("I'm ListModel : notify BEF ?");
        this.notifyObservers();
        //System.out.println("I'm ListModel : notify AFT ?");
    }
    
    public void remove_user(String username, String IP_addr) {
        hmap_users.remove(username + "@" + IP_addr);
        this.notifyObservers();
    }
    
    public String get_IP_addr (String username) {
        return hmap_users.get(username);
    }
    
    public HashMap<String,String> get_hmap_users () {
        return this.hmap_users;
    }
    
    @Override
    public void notifyObservers(){
        this.observer.update(this, this.hmap_users);
    }
    
    @Override
    public void addObserver(Observer o){ 
        this.observer = (ListWindow)o;
    }
}
