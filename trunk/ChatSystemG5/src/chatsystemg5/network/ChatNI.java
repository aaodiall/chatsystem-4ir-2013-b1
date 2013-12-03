package chatsystemg5.network;

import chatsystemg5.brain.ChatController;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ChatNI does the link between the ChatController and the network
 * The ChatController does not know anything about how the network is used
 * The ChatNI has the control on how the message or files are sent or received
 * @author belliot
 */
public /*abstract*/ class ChatNI extends Thread implements Observer {
    
    private ChatController chat_control;
    private MessageHandlerNI msg_handler;
    //private FileHandlerNI file_handler;
    private String username;
    private String IP_new;

    /**
     * The constructor of the ChatNI is instancied with a ChatController's object
     * The username is kept in a variable
     * The MessageHandlerNI is instancied
     * @param chat_control : the same for every object in the ChatSystem
     */
    public ChatNI(ChatController chat_control) {
        
        this.setPriority(MIN_PRIORITY);
        this.chat_control = chat_control;
        username = chat_control.get_userDB().get_username();
        msg_handler = new MessageHandlerNI(/*chat_control, */this);
        //file_handler = new FileHandlerNI(this);
        //System.out.println("I'm ChatNI : username : " + username);

        this.start();
    }

    // _________________________________________________________________________________________
    
    // PARTIE MESSAGE EMISSION
    
    /**
     * This function is used to transfer a connection asked by the ChatController
     * It is then performed by the MessageHandlerNI
     * It uses the remote user's name to get its IP address
     * If it is a new connection, the connection is broadcasted
     * @param user_and_IP : the hello message is sent to this remote user
     * @param alrdythere : the function needs to know if it is a first connection or not
     */
    public void to_connection(String user_and_IP, Boolean alrdythere) {
        try {
            //System.out.println("I'm ChatNI : IP to connection : " + user_and_IP);
            InetAddress IP_dest;
            if (!alrdythere) {
                IP_dest = MessageEmissionNI.get_broadcast();
            }
            else {
                System.out.println("I'm ChatNI : IP to connection : " + chat_control.get_listDB().get_IP_addr(user_and_IP));
                IP_dest = InetAddress.getByName(chat_control.get_listDB().get_IP_addr(user_and_IP));
            }
            System.out.println("I'm ChatNI : IP dest : " + IP_dest.getHostAddress());
            System.out.println("I'm ChatNI : sending a Hello : " + alrdythere);
            msg_handler.send_connection(IP_dest, alrdythere);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatNI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This function is used to transfer a disconnection asked by the ChatController
     * It is then performed by the MessageHandlerNI
     */
    public void to_disconnection() {
        msg_handler.send_disconnection();
    }
    
    /**
     * This function is used to transfer a text message coming from the ChatController
     * It gets the destination IP address thanks to the username given
     * @param username_and_IP : the text message is sent to this remote user
     * @param text : this is the text message
     */
    public void to_send_text(String username_and_IP, String text) {
        try {
            // On récupère l'@IP associée à l'username
            String IP_text = chat_control.get_listDB().get_IP_addr(username_and_IP);
            // On transforme l'@IP de String à InetAddress
            InetAddress IP_dest = InetAddress.getByName(IP_text);
            msg_handler.send_text(IP_dest, text);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatNI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // _________________________________________________________________________________________

    // PARTIE MESSAGE RECEPTION
    
    /**
     * This function is used to transfer a connection to the ChatController coming from the MessageHandlerNI
     * @param remote_user : connection coming from this remote user
     * @param IP_text : IP address of the remote user in a textual style
     * @param alrdythere : indicating if it is a new connection or not
     */
    public void from_connection(String remote_user, String IP_text, Boolean alrdythere) {
        //try {
            System.out.println("I'm ChatNI : IP source received : " + IP_text);
            //System.out.println("I'm ChatNI : connection back : remote user : " + remote_user + ", IP source : " + IP_text + ", already there ? " + alrdythere);
            chat_control.perform_connection_back(remote_user, IP_text, alrdythere);
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(ChatNI.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    /**
     * This function is used to transfer a disconnection to the ChatController coming from the MessageHandlerNI
     * @param remote_user : disconnection coming from this remote user
     * @param IP_text : its IP address is given in a textual style
     */
    public void from_disconnection(String remote_user, String IP_text) {
        chat_control.perform_disconnection(remote_user, IP_text);
    }
    
    /**
     * This function is used to transfer a text message to the ChatController coming from the MessageHandlerNI
     * @param remote_user : text message coming from this remote user
     * @param txt : the text message
     * @param IP_text : its IP address is given in a textual style
     */
    public void from_receive_text(String remote_user, String txt, String IP_text) {
        chat_control.perform_send(remote_user, txt, IP_text);
    }
    
    
    
    /**
     * This function return the username of the local user
     * @return username of the local user
     */
    public String get_user() {
        return username;
    }
    
    /**
     * This function get the state of the user, he is either connected or disconnected
     * @return true (connected) or false (disconnected)
     */
    public boolean get_user_state() {
        return chat_control.get_userDB().get_state();
    }
    
    @Override
    public void update(Observable o, Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
//    /**
//     *
//     * @param IP_text
//     */
//    public void new_connect (String IP_text) {
//        IP_new = IP_text;
//    }
    
    @Override
    public void run(){

        try {
            while(msg_handler.get_user_state()){
                
                this.sleep(10000);
                
                if(msg_handler.get_user_state()){
                    //this.chat_control.get_listDB().set_hmap_users(new HashMap<String, String>());
                    // The temporary list is created
                    chat_control.get_listDB().new_temp();
                    // The controller will now add remote users into the temporary list
                    chat_control.set_buffer_state(true);
                    this.to_connection(username, false);
                }
                System.out.println("I'm ChatNI : CONNECTION CONTROL IS NOW RUNNING");
                this.sleep(10000);
                if(msg_handler.get_user_state()){
                    chat_control.swap_hmap_users();
                    chat_control.set_buffer_state(false);
                }
                System.out.println("I'm ChatNI : CONNECTION CONTROL IS NOW STOPPED............");
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ChatNI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}