package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import java.util.HashMap;


/**
 * Control each frame displayed<p>
 * 
 * Handles operations to the ChatController<p>
 * Handles operations from the ChatModel<p>
 * 
 * chat_control : references the unique ChatController<p>
 * connection_window : references the unique ConnectionWindow<p>
 * list_window : references the unique ListWindow<p>
 * chat_windows : references all the active ChatWindows (not closed by disconnection)<p>
 */
public class ChatGUI {

    private ChatController chat_control;
    
    private ConnectionWindow connection_window;
    private ListWindow list_window;
    private HashMap<String, ChatWindow> chat_windows;

    /**
     * Links the ChatGUI with the ChatController<p>
     * Initializes a new HasmMap<p>
     * Displays the connection window
     * 
     * @param chat_control references the unique ChatController
     */
    public ChatGUI(ChatController chat_control) {
        this.chat_control = chat_control;
        this.chat_windows = new HashMap<String, ChatWindow>();
        
        this.connection_window = new ConnectionWindow(this.chat_control);
    }
    

    /**
     * Create a ChatWindow linked to a specific remote user and add it to the ChatWindows list<p>
     * If a ChatWindow linked to the remote user doesn't exist yet then it creates a new ChatWindow linked to this user<p>
     * Else nothing happens
     * 
     * @param id references the remote username and IP used to link the ChatWindow
     */
    public void create_chat_window(String id){
        if(!this.chat_windows.containsKey(id)){
            ChatWindow chat_window = new ChatWindow(this.chat_control, id);
            this.chat_windows.put(id, chat_window);
            this.chat_windows.get(id).setVisible(true);
        } 
    }

    /**
     * Delete a ChatWindow linked to a specific remote user from the ChatWindows list<p>
     * If the ChatWindow linked to the specific user exists then it removes it<p>
     * Else nothing happens
     * 
     * @param id references the remote username and IP used to link the ChatWindow
     */
    public void delete_chat_window(String id){
        if(this.chat_windows.containsKey(id)){
            this.chat_windows.remove(id);
        }
    }
        
    /**
     * Allows to get a ChatWindow linked to a specific user from the ChatWindows list<p>
     * If the ChatWindow linked to the specific user exists then it returns it<p>
     * Else it returns null
     * 
     * @param id references the remote username and IP used to link the ChatWindow to it
     * @return a specific ChatWindow
     */
    public ChatWindow get_chat_window(String id){
        if(this.chat_windows.containsKey(id)){
            return this.chat_windows.get(id);
        }
        else {
            return null;
        }
    }

    /**
     * Allows to get the ListWindow
     * 
     * @return the listWindow
     */
    public ListWindow get_list_window() {
        return this.list_window;
    }
    
    /**
     * Initializes the a ListWindow
     */
    public void init_list_window(){
        this.list_window = new ListWindow(this.chat_control);
    }
    
    /**
     * Allows to get the ChatWindows list<p>
     * If the ChatWindows List isn't null then it returns it<p>
     * Else it returns null
     * @return the ChatWindows list
     */
    public HashMap<String, ChatWindow> get_chat_windows() {
        if(this.chat_windows != null){
            return this.chat_windows;
        }
        else {
        return null;
        }
    }

    /**
     * Allows to get the ConnectionWindow
     * 
     * @return the ConnectionWindow
     */
    public ConnectionWindow get_connection_window() {
        return this.connection_window;
    }
}
