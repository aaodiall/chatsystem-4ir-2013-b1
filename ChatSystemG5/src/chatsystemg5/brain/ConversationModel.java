/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.brain;

import chatsystemg5.ihm.ChatWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observer;


public class ConversationModel extends ChatModel{
    private HashMap<String, LinkedList<String>> conversation;
    
    ConversationModel (ChatController chat_control){
        this.conversation = new HashMap<String, LinkedList<String>>();
        
        // add list window as listModel obersver
        //this.addObserver(chat_control.get_chatGUI().get_chat_window());
    }
    
    public void add_conversation(String remote_user, String IP_text, String text) {
        // if username already in the list
        if (this.conversation.containsKey(remote_user)) {
            // just add the message at the end of his message list
            this.conversation.get(remote_user).add(text);
        }
        else {
            LinkedList messages = new LinkedList<String>();
            messages.add(text);
            this.conversation.put(remote_user, messages);
        }
        this.notifyObservers();
    }

    @Override
    public void notifyObservers(){ 
        // because there is only one observer
        // if there were several ones we had to implement a loop updating each Observer
        this.observer.update(this, this.conversation);
    } 
}
