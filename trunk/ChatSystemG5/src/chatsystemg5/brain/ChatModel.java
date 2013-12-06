	package chatsystemg5.brain;

import java.util.Observable;
import java.util.Observer;

/**
 * Parent class for different kind of models
 */
public abstract class ChatModel extends Observable{
    
   /**
     * Adds an observer to the obersvable
     * @param o the observer
     */
    @Override
    public abstract void addObserver(Observer o);
    
    /**
     * Removes observers of the observable
     */
    @Override
    public abstract void deleteObservers();
    
    /**
     * Calls the update method of observers<p>
     */
    @Override
    public abstract void notifyObservers();
}
