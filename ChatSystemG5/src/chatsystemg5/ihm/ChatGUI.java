package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import java.util.HashMap;
import javax.swing.JFrame;

/**
 *
 * @author belliot
 */
public class ChatGUI extends JFrame {

    private ChatController chat_control;
    private ConnectionWindow connection_window;
    private ListWindow list_window;
    private HashMap<String, ChatWindow> chat_windows;

    /**
     *
     * @param chat_control
     */
    public ChatGUI(ChatController chat_control) {
        this.chat_control = chat_control;
        this.chat_windows = new HashMap<String, ChatWindow>();
        
        this.connection_window = new ConnectionWindow(this.chat_control);
    }
    
    /**
     *
     * @param remote_username
     */
    public void create_chat_window(String remote_username){
        if(!this.chat_windows.containsKey(remote_username)){
            ChatWindow chat_window = new ChatWindow(this.chat_control, remote_username);
            this.chat_windows.put(remote_username, chat_window);
            this.chat_windows.get(remote_username).setVisible(true);
        } 
    }
    
    /**
     *
     * @param remote_username
     */
    public void delete_chat_window(String remote_username){
        this.chat_windows.remove(remote_username);
    }
    
        
    /**
     *
     * @param remote_username
     * @return
     */
    public ChatWindow get_chat_window(String remote_username){
        if(this.chat_windows.containsKey(remote_username)){
            return this.chat_windows.get(remote_username);
        }
        else {
            return null;
        }
    }

    /**
     *
     * @return
     */
    public ListWindow get_list_window() {
        return this.list_window;
    }
    
    /**
     *
     */
    public void init_list_window(){
        this.list_window = new ListWindow(this.chat_control);
    }
    
    /**
     *
     * @return
     */
    public HashMap<String, ChatWindow> get_chat_windows() {
        if(this.chat_windows != null){
            return this.chat_windows;
        }
        return null;
    }

    /**
     *
     * @return
     */
    public ConnectionWindow get_connection_window() {
        return this.connection_window;
    }
}
