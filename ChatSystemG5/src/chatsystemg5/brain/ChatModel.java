	package chatsystemg5.brain;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class ChatModel extends Observable {
    
    protected Observer observer;
    
    public ChatModel (){
    }

    public abstract void notifyObservers();
    public void addObserver(Observer o) {
        this.observer = o;
    }
    
}
