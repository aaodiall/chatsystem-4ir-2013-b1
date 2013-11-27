package chatsystemg5.brain;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Iterator;

public class UserModel extends ChatModel {
    
    private ArrayList<Observer> observers;

    private String username;
    private Boolean status;
    
    public UserModel (String username) {
        this.username = username;
        status = true;
        this.observers = new ArrayList<Observer>();
    }
    
    public String get_username(){
        return this.username;
    }
    
    public void set_username(String username){
        this.username = username;
    }
    
    public boolean get_state() {
        return status;
    }
    
    public void set_state(boolean connected) {
        this.status = connected;
    }
    
    @Override
    public void notifyObservers(){
        for(Iterator<Observer> it = this.observers.iterator(); it.hasNext();){
            it.next().update(this, this.username);
        }
    }
    
    @Override
    public void addObserver(Observer o){ 
        this.observers.add((Observer)o);
    }
}
