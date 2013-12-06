package chatsystemg5.brain;

import java.util.Observer;

/**
 * Represents the UserModel<p>
 * Handles the local user username and status<p>
 * 
 * username : references the local user username<p>
 * status : references the local user state : connected or disconnected
 */
public class UserModel extends ChatModel {

    private String username;
    private Boolean status;
    
    /**
     * Initilizes the UserModel next to the local user connection<p>
     * Gets the username input in the ConnectionWindow<p>
     * Set the status to connected by default at the initilization
     * 
     * @param username the username input of the ConnectionWindow
     */
    public UserModel (String username) {
        this.username = username;
        this.status = true;
    }
    
    /**
     * Allows to get the local user username
     * @return the local user username
     */
    public String get_username(){
        return this.username;
    }
    
    /**
     * Allows to set the local user username
     * @param username the local user username
     */
    public void set_username(String username){
        this.username = username;
    }
    
    /**
     * Allows to get the local user state
     * @return the local user state
     */
    public boolean get_state() {
        return status;
    }
    
    /**
     * Allow to set the local user state
     * @param connected the local user state
     */
    public void set_state(boolean connected) {
        this.status = connected;
    }

    @Override
    public void addObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteObservers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
