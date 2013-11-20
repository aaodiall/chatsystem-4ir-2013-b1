    package chatsystemg5.brain;

import chatsystemg5.ihm.ListWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.ListDataListener;


public class ListModel extends ChatModel {
    
    private HashMap<String,String> hmap_users;
    
    public ListModel (ChatController chatController) {
        hmap_users = new HashMap();

        // add list window as listModel obersver
        this.addObserver(chatController.get_chatGUI().get_list_window());
    }
    
    public void add_user(String username, String IP_addr) {
        if (!this.hmap_users.containsKey(username + "@" + IP_addr)) {
            hmap_users.put (username + "@" + IP_addr, IP_addr);
        }
    }
    
    public void remove_user(String username, String IP_addr) {
        hmap_users.remove(username + "@" + IP_addr);
    }
    
    public String get_IP_addr (String username) {
        return hmap_users.get(username);
    }

    @Override
    public void notifyObservers(){ 
        // because there is only one observer
        // if there were several ones we had to implement a loop updating each Observer
        this.observer.update(this, this.hmap_users);
    }        
}
