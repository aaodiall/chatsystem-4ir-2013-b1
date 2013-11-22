package chatsystemg5.brain;

import java.util.ArrayList;
import java.util.Observer;

public class UserModel extends ChatModel {
    
    private ArrayList<Observer> observers;

    private String username;
    private Boolean status;
    
    public UserModel (String username) {
        this.username = username;
        status = true;
    }
    
    public String get_username(){
        return this.username;
    } 
}
