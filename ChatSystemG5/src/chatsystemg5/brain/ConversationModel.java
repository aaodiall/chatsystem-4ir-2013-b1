package chatsystemg5.brain;

import chatsystemg5.ihm.ChatWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observer;

/**
 * Represents the ConversationModel<p>
 * Handles multiple conversations : one for each used ChatWindow<p>
 * 
 * Implements Observable to notify the ChatWindow when change occurs<p>

 * chat_control : references the unique ChatController<p>
 * conversations : references all the text stored in a conversations between the local user and a remote user<p>
 * observers : references the collection of observers linked to the ConversationModel
 */
public class ConversationModel extends ChatModel {
    
    private ChatController chat_control;
    
    private HashMap<String, LinkedList<String>> conversations;    
    
    private ArrayList<ChatWindow> observers;
    /**
     * Links the ConversationModel to the unique controller<p>
     * Initializes a new conversations collection<p>
     * Initializes a new observers collection thaht correspond to ChatWindow<p>
     * 
     * @param chat_control references the unique ChatController
     */
    ConversationModel (ChatController chat_control){
        this.conversations = new HashMap<String, LinkedList<String>>();
        this.chat_control = chat_control;
        this.observers = new ArrayList<ChatWindow>();
    }
    
    /**
     * Username and IP are used like an ID to link each conversations to its specific ChatWindow<p>
     * Checks if the ID already exists in the conversations collection (so is linked to a ChatWindow)<p>
     * If it exists then text is added to the conversations collection and linked to the correct ChatWindow<p>
     * Else it means that it's the remote user who initializes the conversations so a new one is created<p>
     * Notifies the specific ChatWindow of the change in the conversations
     * 
     * 
     * @param id represents a remote username and his IP separated by "@"
     * @param text represents the message sent by the remote user
     */
    public void add_conversation(String id, String text) {
        // if username already in the list
        if (!this.conversations.containsKey(id)) {
            LinkedList messages = new LinkedList<String>();
            this.conversations.put(id, messages);
        }
        // just add the message at the end of his message list
        this.conversations.get(id).add(text);

        this.notifyObservers(id);
    }
    
    /**
     * Allows to get the conversations attribute
     * @return the conversations attribute
     */
    public HashMap<String, LinkedList<String>> get_conversation() {
        return this.conversations;
    }
    
    /**
     * Allows to get the last text off a conversations using the associated username and IP
     * @param id represents a remote username and his IP separated by "@"
     * @return the last text of the conversations between local user and id
     */
    public String get_last_text(String id){
        return this.conversations.get(id).getLast();
    }
    
    /**
     * Calls the update method of a specific ChatWindow<p>
     * Uses the id to specify which ChatWindow update
     * @param arg represents a remote username and his IP separated by "@"
     */
    @Override
    public void notifyObservers(Object arg){
        this.chat_control.get_chatGUI().get_chat_window((String)arg).update(this, this.conversations);
    }
    
    /**
     * Adds a ChatWindow to the collection of observers
     * @param o a ChatWindow
     */
    @Override
    public void addObserver(Observer o){ 
        this.observers.add((ChatWindow)o);
    }
    
    /**
     * Removes all the observers
     */
    @Override
    public void deleteObservers(){
        for(Iterator<ChatWindow> it = this.observers.iterator(); it.hasNext();){
            it.next().dispose();
        }
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
