package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;
import java.awt.BorderLayout;
import java.awt.Panel;

public class ChatGUI /*extends Panel*/{
    
    private ChatController chat_control;
    //private Panel layout;
    private ConnectionWindow connection_window;
    private ListWindow list_window;
    private ChatWindow chat_window;
    
    public ChatGUI(ChatController chat_control){
        this.chat_control = chat_control;
        //this.layout = new Panel();
        //this.layout.setLayout(new BorderLayout());
        
        this.connection_window = new ConnectionWindow(this.chat_control);  
        this.list_window = new ListWindow(this.chat_control);
        this.chat_window = new ChatWindow(this.chat_control);
        this.chat_window.setVisible(false);
        
        //this.layout.addLayoutComponent(this.connection_window,"north");
        //this.layout.addLayoutComponent(this.list_window.get_JList(),BorderLayout.CENTER);
        //this.layout.add(this.chat_window,BorderLayout.SOUTH);
    }
    
    public ListWindow get_list_window(){
        return this.list_window;
    }
    
    public ChatWindow get_chat_window(){
        return this.chat_window;
    }
    
    public ConnectionWindow get_connection_window(){
        return this.connection_window;
    }
}
