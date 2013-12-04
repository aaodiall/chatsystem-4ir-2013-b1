package chatsystemg5.brain;
import chatsystemg5.ihm.ChatGUI;
import chatsystemg5.network.ChatNI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Represents the ChatController<p>
 * Handles operations to and from the ChatNI<p>
 * Handles operations to and from the ChatGUI<p>
 * Handles operations to the ChatModel<p>

 * userDB : references the unique UserModel<p>
 * listDB references the unique ListModel<p>
 * convDB references the unique ConversationModel<p>
 * username : refererences the username in the UserModel<p>
 * chatGUI : references the unique ChatGUI<p>
 * chatNI : references the unique ChatNI<p>
 * buffer_state : references to a state in which controller check conenction to remote users
 */
public class ChatController {

    private UserModel userDB;
    private ListModel listDB;
    private ConversationModel convDB;
    
    private String username;
    
    private ChatGUI chatGUI;
    private ChatNI chatNI;
    
    private boolean buffer_state;
    
    /**
     * Creates the ChatController
     * Creates the ChatGUI (to display the ConnectionWindow)
     * Set by default the buffer_state to false
     */
    public ChatController () {
        chatGUI = new ChatGUI(this);
        buffer_state = false;  
    }

    /**
     * Initializes the ChatController next to the local user connection<p>
     * Initializes the UserModel with the username input from the ConnectionWindow<p>
     * Initializes the ListModel and the ConversationModel<p>
     * Set the username<p>
     * Initializes the network configuration<p>
     * Initializes the ListWindow and links it to the ListModel<p>
     * Performs connection to let remote users know that local user is now connected
     * 
     * @param username references the local user username
     */
    public void init_controller (String username) {
        // On crée la BDD User
        userDB = new UserModel(username);
        
        // initialize model
        listDB = new ListModel(this);
        convDB = new ConversationModel(this);
        
        this.username = this.userDB.get_username();
        
        // initialize network
        chatNI = new ChatNI(this);
        
        chatGUI.init_list_window();
        listDB.addObserver(chatGUI.get_list_window());

        this.perform_connection();
    }
    
   
    /**
     * Performs connection of the local user
     * Set the local user st





ate to connected
     */
    public void perform_connection () {
        this.userDB.set_state(true);
        chatNI.to_connection(username, false);
    }
    
    /**
     * Handles the connection of a remote user<p>
     * Checks if the ChatController is practising "check remote user connection"<p>
     * If no, the remote user username and IP are stored in user list<p>
     * Else remote user username and IP are stored in the temporary user list<p>
     * 
     * If it's remote user first connection then sends back a hello<p>
     * Else nothing happens
     * 
     * @param remote_user referecens the remote user username 
     * @param IP_address references the remote user IP address
     * @param alrdythere referecences what kind of hello is received
     */
    public void perform_connection_back (String remote_user, String IP_address, Boolean alrdythere) {
        if (!buffer_state)  {
            listDB.add_user(remote_user, IP_address);
        }
        else {
            listDB.add_user_temp(remote_user, IP_address);
        }
        
        if(!alrdythere){
            System.out.println("I'm Controller : sending back a Hello to : " + remote_user + " @ " + IP_address);
            chatNI.to_connection(remote_user + "@" + IP_address, true);       
        }
     }
    
    /**
     * Swaps the user list with the temporary user list<p>
     * Notifies the list observer
     */
    public void swap_hmap_users () {
        listDB.get_hmap_users().clear();
        listDB.get_hmap_users().putAll(listDB.get_hmap_temp());
        listDB.get_hmap_temp().clear();
        listDB.notifyObservers();
    }
     
    /**
     * Performs disconnection from the local user<p>
     * Sets user state to disconnected<p>
     * Tells the ChatNI to send a disconnection<p>
     * Removes observers of the listDB<p>
     * Deletes the listDB<p>
     * Removes observers of the convDB<p>
     * Deletes the convDB<p>
     * Removes the displayed ChatWindows
     * Deletes the username<p>
     * Stops the ChatNI
     */
    public void perform_disconnection () {
        this.userDB.set_state(false);
        
        this.chatNI.to_disconnection();
        
        this.listDB.deleteObservers();
        this.listDB = null;

        this.convDB.deleteObservers();
        this.convDB = null;
        this.chatGUI.get_chat_windows().clear();
               
        this.userDB.set_username(null);
        
        this.chatNI.interrupt(); 
     }
    
    /**
     * Performs disconnection from a remote user<p>
     * Removes the remote user from the user list<p>
     * If a ChatWindow with this remote user were openned then closes it<p>
     * 
     * @param remote_user references the remote user username
     * @param IP_address references the remote user IP address
     */
    public void perform_disconnection (String remote_user, String IP_address) {
        listDB.remove_user(remote_user, IP_address);
        
        if (this.get_chatGUI().get_chat_window(remote_user + "@" + IP_address) != null) {
            this.get_chatGUI().get_chat_window(remote_user + "@" + IP_address).dispose();
            this.get_chatGUI().get_chat_windows().remove(remote_user + "@" + IP_address);
        } 
    }
    
    /**
     * Performs send message from the local user
     * 
     * Removes the white spaces before and after the text to send
     * Checks if the message isn't just a blank
     * If it's not blank then dd the "YOU : " label before each message send from the local user and adds the text to the ConversationModel of the specific ChatWindow
     *
     * @param username_and_IP references the remote username and his IP separated by "@"
     * @param text references the text to send
     */
    
    // Envoi d'un messsage texte
    public void perform_send (String username_and_IP, String text) {
        String message = text.trim();
        if (!message.isEmpty()){
            chatNI.to_send_text(username_and_IP, message);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            message = "[" + dateFormat.format(date) + "]\n" + message + "\n\n";
            this.get_convDB().add_conversation(username_and_IP, "YOU : " + message + "\n\n");
            
        }
    }
    
    /**
     * Performs receive message from the remote user<p>
     * Adds date and time to the message<p>
     * Checks in the ConversationModel if the conversation with this specific remote user has already started<p>
     * If yes then adds the text to the conversation<p>
     * Else then create a new ChatWindow and a new conversation and adds the text to it
     * 
     * @param remote_user references the remote user username
     * @param txt references the remote user text message
     * @param IP_address references the remote user IP address
     */
    public void perform_send (String remote_user, String txt, String IP_address) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String message = "[" + dateFormat.format(date) + "]\n" + txt + "\n\n";
        String id = remote_user + "@" + IP_address;
        
        if (convDB.get_conversation().containsKey(id)) {
            // just add the message at the end of his message list
            convDB.add_conversation(id, message);
        }
        else {
            if(!chatGUI.get_chat_windows().containsKey(id)){
                chatGUI.create_chat_window(id);
            }
            convDB.add_conversation(id, message);
        }
    }
        
    /**
     * Allows to get the userDB
     * @return the userDB
     */
    
    public UserModel get_userDB(){
        return this.userDB;
    }
    
    /**
     * Allows to get the listDB
     * @return the listDB
     */
    public ListModel get_listDB(){
        return this.listDB;
    }
    
    /**
     * Allows to get the convDB
     * @return the convDB
     */
    public ConversationModel get_convDB(){
        return this.convDB;
    }
 
    /**
     * Allows to get the chatGUI
     * @return the chatGUI
     */
    public ChatGUI get_chatGUI(){
        return this.chatGUI;
    }
    
    /**
     * Allows to get the chatNI
     * @return the chatNI
     */
    public ChatNI get_chatNI(){
        return this.chatNI;
    }
    
    /**
     * Allows to set the buffer_state
     * @param state references if the ChatController is in check remote user connection mode
     */
    public void set_buffer_state (boolean state) {
        this.buffer_state = state;
    }
}