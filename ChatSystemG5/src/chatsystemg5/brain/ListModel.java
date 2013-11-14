package chatsystemg5.brain;

import java.util.HashMap;

public class ListModel extends ChatModel {
    
    HashMap<String,String> hmap_users;
    
    public ListModel () {
        hmap_users = new HashMap();
    }
    
    public void add_user(String username, String IP_addr) {
        hmap_users.put (username + "-" + IP_addr, IP_addr);
    }
    
    public void remove_user(String username, String IP_addr) {
        hmap_users.remove(username + "-" + IP_addr);
    }
    

    @Override
    public void getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subjectState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
