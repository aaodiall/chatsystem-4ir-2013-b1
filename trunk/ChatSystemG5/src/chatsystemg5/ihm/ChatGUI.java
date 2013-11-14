package chatsystemg5.ihm;

import chatsystemg5.brain.ChatController;

public class ChatGUI {
    private ChatController chatController;
    //private ConnectionWindow connectionWindow;
    private ListWindow listWindow;
    //private ChatWindow chatWindow;
    
    public ChatGUI(ChatController chatController){
        this.chatController = chatController;
        //this.connectionWindow = new ConnectionWindow();
        this.listWindow = new ListWindow(this.chatController.get_listDB());
        //this.chatWindow = new ChatWindow();
    }
    
    public ListWindow get_listWindow(){
        return this.listWindow;
    }
}
