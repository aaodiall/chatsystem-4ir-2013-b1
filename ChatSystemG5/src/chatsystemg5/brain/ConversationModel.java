/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.brain;

import chatsystemg5.ihm.ChatWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;


public class ConversationModel extends ChatModel{
    private HashMap<String, ArrayList<String>> conversation;
    private Observer observer;
    
    ConversationModel (ChatController chat_control){
        this.conversation = new HashMap<String, ArrayList<String>>();
        
        // add list window as listModel obersver
        this.addObserver(chat_control.get_chatGUI().get_chat_window());
    }
    
    public void add_conversation(String username, String text) {
        // if username already in the list
        if (this.conversation.containsKey(username)) {
            // just add the message at the end of his message list
            this.conversation.get(username).add(text);
        }
        else {
            ArrayList messages = new ArrayList<String>();
            messages.add(text);
            this.conversation.put(username, messages);
        }
        this.notifyObservers();
    }

    @Override
    public void addObserver(Observer o) {
        this.observer = o;
    }

    @Override
    public void notifyObservers(){ 
        // because there is only one observer
        // if there were several ones we had to implement a loop updating each Observer
        this.observer.update(this, this.conversation);
    } 
}
