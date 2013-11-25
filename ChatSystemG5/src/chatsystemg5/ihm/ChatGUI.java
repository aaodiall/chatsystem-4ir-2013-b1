package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import java.awt.BorderLayout;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChatGUI extends JFrame {

    private ChatController chat_control;
    private ConnectionWindow connection_window;
    private ListWindow list_window;
    private HashMap<String, ChatWindow> chat_windows;

    public ChatGUI(ChatController chat_control) {
        this.chat_control = chat_control;
        this.chat_windows = new HashMap<String, ChatWindow>();
        
        this.connection_window = new ConnectionWindow(this.chat_control);
    }
    
    public void create_chat_window(String remote_username){
        if(!this.chat_windows.containsKey(remote_username)){
            ChatWindow chat_window = new ChatWindow(this.chat_control, remote_username);
            this.chat_windows.put(remote_username, chat_window);
        }
    }
    
    public void delete_chat_window(String remote_username){
        this.chat_windows.remove(remote_username);
    }
    
        
    public ChatWindow get_chat_window(String remote_username){
        if(this.chat_windows.containsKey(remote_username)){
            return this.chat_windows.get(remote_username);
        }
        else {
            System.err.println("The " + remote_username + " ChatWindow doesn't exist");
            return null;
        }
    }

    public ListWindow get_list_window() {
        return this.list_window;
    }
    
    public void init_list_window(){
        this.list_window = new ListWindow(this.chat_control);
    }
    
    public HashMap<String, ChatWindow> get_chat_windows() {
        return this.chat_windows;
    }

    public ConnectionWindow get_connection_window() {
        return this.connection_window;
    }
}
