package chatsystemg5.brain;

import chatsystemg5.ihm.ListWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

// implements Observable
public class ListModel extends ChatModel {
    
    private HashMap<String,String> hmap_users;
    private ArrayList<Observer> observers;
    
    public ListModel (ChatController chatController) {
        hmap_users = new HashMap();
        
        // add list window as listModel obersver
        this.addObserver(chatController.get_chatGUI().get_listWindow());
    }
    
    public void add_user(String username, String IP_addr) {
        if (!this.hmap_users.containsKey(username + "-" + IP_addr)) {
            hmap_users.put (username + "-" + IP_addr, IP_addr);
        }
    }
    
    public void remove_user(String username, String IP_addr) {
        hmap_users.remove(username + "-" + IP_addr);
    }
    
    public HashMap get_hashmap_users(){
        return this.hmap_users;
    }

    @Override
    public void getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // TO DO !! find the way to make it work for general purpose : connection, disconnection
    @Override
    public void setState() {
        this.add_user("Joffrey", "34.34.34.34");
    }

    @Override
    public void subjectState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
