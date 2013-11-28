/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystemg5.brain;

import chatsystemg5.ihm.ChatWindow;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observer;


public class ConversationModel extends ChatModel {
    
    private ArrayList<ChatWindow> observers;
    
    private HashMap<String, LinkedList<String>> conversation;
    private ChatController chat_control;
    
    ConversationModel (ChatController chat_control){
        this.conversation = new HashMap<String, LinkedList<String>>();
        this.chat_control = chat_control;
        this.observers = new ArrayList<ChatWindow>();
        
        // add list window as listModel obersver
        //this.addObserver(chat_control.get_chatGUI().get_chat_window());
    }
    
    public void add_conversation(String remote_user_and_ip, String text) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String txt = "[" + dateFormat.format(date) + "]\n" + text + "\n\n";
        // if username already in the list
        if (this.conversation.containsKey(remote_user_and_ip)) {
            // just add the message at the end of his message list
            this.conversation.get(remote_user_and_ip).add(txt);
        }
        else {
            if(!this.chat_control.get_chatGUI().get_chat_windows().containsKey(remote_user_and_ip)){
                this.chat_control.get_chatGUI().create_chat_window(remote_user_and_ip);
            }
            LinkedList messages = new LinkedList<String>();
            messages.add(txt);
            this.conversation.put(remote_user_and_ip, messages);
        }
        this.notifyObservers(remote_user_and_ip);
    }
    
    public String get_last_text_by_user(String remote_username){
        return this.conversation.get(remote_username).getLast();
    }
    
    @Override
    public void notifyObservers(Object arg){
        this.chat_control.get_chatGUI().get_chat_window((String)arg).update(this, this.conversation);
    }
    
    @Override
    public void addObserver(Observer o){ 
        this.observers.add((ChatWindow)o);
    }
    
    @Override
    public void deleteObservers(){
        for(Iterator<ChatWindow> it = this.observers.iterator(); it.hasNext();){
            it.next().dispose();
        }
    }
}
