	package chatsystemg5.brain;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class ChatModel extends Observable {
    
    public ChatModel (){
    }

    public abstract void notifyObservers();
    public abstract void addObserver(Observer o);
    
}
