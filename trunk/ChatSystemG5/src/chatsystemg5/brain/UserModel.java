package chatsystemg5.brain;

import java.util.Observer;

public class UserModel extends ChatModel {
    
    private Observer observer;

    private String username;
    private Boolean status;
    
    public UserModel (String username) {
        this.username = username;
        status = true;
    }
    
    public String get_username(){
        return this.username;
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}
