package chatsystemg5.brain;
import chatsystemg5.ihm.ChatGUI;
import chatsystemg5.network.ChatNI;


/**
 *
 * @author belliot
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
     *
     */
    public ChatController () {
        // initialize view
        // TO DO create view at the end
        chatGUI = new ChatGUI(this);
        buffer_state = false;
        
    }
    
    /**************** Controller init ***************
     * @param username 
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
    }
    
   
    /**************** Connection ****************/
    
    // Connexion du local user
    public void perform_connection () {
        this.userDB.set_state(true);
        chatNI.to_connection(username, false);
    }
    
    // Connexion d'un autre user
    /**
     *
     * @param remote_user
     * @param IP_text
     * @param alrdythere
     */
    public void perform_connection_back (String remote_user, String IP_text, Boolean alrdythere) {
        if (!buffer_state)  {
            //System.out.println("I'm Controller, receiving connection : remote user : " +remote_user + ", IP source : " + IP_text + ", already there ? " + alrdythere);
            // Ajouter utilisateur au model
            listDB.add_user(remote_user, IP_text);
        }
        else {
            listDB.add_user_temp(remote_user, IP_text);
        }
        
        // if remote user first connection
        if(!alrdythere){
            System.out.println("I'm Controller : sending back a Hello to : " + remote_user + " @ " + IP_text);
            // create a new message to sent back ack
            chatNI.to_connection(remote_user + "@" + IP_text, true);       
        }
     }
    
    // Swap the 2 hmap list
    /**
     *
     */
    public void swap_hmap_users () {
        listDB.get_hmap_users().clear();
        listDB.get_hmap_users().putAll(listDB.get_hmap_temp());
        listDB.get_hmap_temp().clear();
        listDB.notifyObservers();
    }
     
    /**************** Disonnection ****************/
     
    // Déconnection du local user
    public void perform_disconnection () {
        this.userDB.set_state(false);
        chatNI.to_disconnection();
        
        // no need to call the garbage collector
        this.listDB.deleteObservers();
        this.listDB = null;
        //this.chatGUI.get_list_window().dispose();
        this.convDB.deleteObservers();
        this.convDB = null;
        this.chatGUI.get_chat_windows().clear();
               
        this.userDB.set_username(null);
        this.chatNI.interrupt();
        
     }
    
    // Déconnection d'un autre user
    /**
     *
     * @param remote_user
     * @param IP_text
     */
    public void perform_disconnection (String remote_user, String IP_text) {
        // remove the user who disconnected from the list
        listDB.remove_user(remote_user, IP_text);
        if (this.get_chatGUI().get_chat_window(remote_user + "@" + IP_text) != null) {
            this.get_chatGUI().get_chat_window(remote_user + "@" + IP_text).dispose();
            this.get_chatGUI().get_chat_windows().remove(remote_user + "@" + IP_text);
        } 
    }
    
    /**************** Communication by text ***************
     * @param username_and_IP
     * @param text  
     */
    
    // Envoi d'un messsage texte
    public void perform_send (String username_and_IP, String text) {
        chatNI.to_send_text(username_and_IP, text);
    }
    
    // Réception d'un message
    /**
     *
     * @param remote_user
     * @param txt
     * @param IP_text
     */
    public void perform_send (String remote_user, String txt, String IP_text) {
        //display_message ()
        convDB.add_conversation(remote_user + "@" + IP_text, txt);  
    }
        
    /**************** Getters ***************
     * @return 
     */
    
    public UserModel get_userDB(){
        return this.userDB;
    }
    
    /**
     *
     * @return
     */
    public ListModel get_listDB(){
        return this.listDB;
    }
    
    /**
     *
     * @return
     */
    public ConversationModel get_convDB(){
        return this.convDB;
    }
 
    /**
     *
     * @return
     */
    public ChatGUI get_chatGUI(){
        return this.chatGUI;
    }
    
    /**
     *
     * @return
     */
    public ChatNI get_chatNI(){
        return this.chatNI;
    }
    
    /**
     *
     * @param state
     */
    public void set_buffer_state (boolean state) {
        this.buffer_state = state;
    }
}
