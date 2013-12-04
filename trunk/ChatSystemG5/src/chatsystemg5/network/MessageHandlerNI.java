package chatsystemg5.network;

import chatSystemCommon.*;
import java.net.InetAddress;

/**
 * The MessageHandlerNI is the intermediate between the ChatNI and the emission and reception of messages only
 * with the UDP protocol
 * It knows the Message type to handle
 * It builds messages for the emission and disassemble messages for the reception
 * @author belliot
 */
public class MessageHandlerNI {
    
    private int UDP_port;
    private ChatNI chatNI; 
    String username;
    InetAddress IP_dest;
    private MessageEmissionNI msg_emission;
    private Thread msg_reception;
    
    //private ChatController chat_control;
    
    /**
     * The MessageHandlerNI is instancied with a unique ChatNI
     * The UDP port is set to 16 001
     * The ChatNI and the username are kept in a variable
     * The reception part is created with a Thread object, the MessageHandlerNI and the UDP port
     * The MessageEmissionNI's thread is started
     * The emission part is created with the MessageHandlerNI and the UDP port
     * @param chatNI : the same for every object in the network package
     */
    public MessageHandlerNI (/*ChatController chat_control, */ChatNI chatNI) {
        
        //this.chat_control = chat_control;
        
        UDP_port = 16001;
        this.chatNI = chatNI;
        username = chatNI.get_user();
        
        System.out.println("I'm MsgHandler : view of thread BEF : " + msg_reception);
        // Creation de la rececption
        if (msg_reception == null) {
            System.out.println("Thread créé !");
            msg_reception = new Thread(new MessageReceptionNI(this, UDP_port));
        }
        System.out.println("I'm MsgHandler : view of thread AFT : " + msg_reception);
        msg_reception.start();
        
        // Creation de l'emission
        msg_emission = new MessageEmissionNI(UDP_port);
        
    }
    
    /**
     * This function get the state of the user, he is either connected or disconnected
     * @return true (connected) or false (disconnected)
     */
    public boolean get_user_state() {
        return chatNI.get_user_state();
    }
    
    // _________________________________________________________________________________________
    
    // Gère la liaison reception-chatNI
    
    /**
     * Does the link between the MessageReceptionNI and the ChatNI
     * It receives a message and determines its kind (Hello, Goodbye or Text)
     * It transforms the IP address in a textual style
     * Then it transfers the message to the ChatNI after desassembling it
     * @param msg : message received by the MessageReceptionNI
     * @param IP_source : IP address of the remote user where the message comes from
     */
    public void receive (Message msg, InetAddress IP_source) {
        String IP_text = IP_source.getHostAddress();
        if (msg instanceof Hello) {
            System.out.println("I'm MsgHandler : Hello received from " + msg.getUsername() + ", IP source : " + IP_text);
            //chatNI.new_connect(IP_text);
            chatNI.from_connection(msg.getUsername(), IP_text, ((Hello) msg).isAck());
            //chat_control.perform_connection(msg.getUsername(), IP_text, ((Hello) msg).isAck());
        }
        if (msg instanceof Goodbye) {
            System.out.println("I'm MsgHandler : Goodbye received.");
            chatNI.from_disconnection(msg.getUsername(), IP_text);
        }
        if (msg instanceof Text) {
            System.out.println("I'm MsgHandler : Text received.");
            chatNI.from_receive_text(msg.getUsername(), ((Text) msg).getText(), IP_text);
        }
    }
    

    // _________________________________________________________________________________________   
    
    // Gère la liaison chatNI-emission
    
    /**
     * Does the link between the ChatNI and the MessageEmissionNI
     * The message is already created and the IP address is already efficient
     * It chooses how the message has to be send according to its type
     * @param msg : message to send 
     * @param IP_dest : the receiver remote user
     */ 
    public void send(Message msg, InetAddress IP_dest){
        if (!IP_dest.isLoopbackAddress()){
            // Si le message est de type Hello
            if (msg instanceof Hello) {
                msg_emission.transfer_connection((Hello) msg, IP_dest);
            }
            if (msg instanceof Goodbye) {
                msg_emission.transfer_disconnection((Goodbye) msg, IP_dest);
            }
            if (msg instanceof Text) {
                msg_emission.send_text((Text) msg, IP_dest);
            }
        }
        else {
             System.out.println("Remote user unreachable");
         }
    }
    
    /**
     * Creates a Hello message thanks to the username and a boolean (false if new connection, else true)
     * @param IP_dest : IP address of the receiver
     * @param alrdythere : false if new connection, else true
     */
    public void send_connection(InetAddress IP_dest, Boolean alrdythere) {
        //System.out.println("I'm MsgHandler : username : " + username);
        Hello msg = new Hello(username, alrdythere);
//        if (!alrdythere) {
//            IP_dest = msg_emission.get_broadcast();
//        }
        //System.out.println("I'm MsgHandler : sending Hello to " + IP_dest.getHostAddress());
        send(msg, IP_dest);
    }
    
    /**
     * Creates a Goodbye message thanks to the username
     * It is broadcasted
     */
    public void send_disconnection() {
        Goodbye msg = new Goodbye(username);
        IP_dest = msg_emission.get_broadcast();
        send(msg, IP_dest);
        this.msg_reception.interrupt();        
     }
    
    /**
     * Creates a Text message thanks to the IP address of the receiver and the text
     * @param IP_dest : receiver's IP address
     * @param txt : text to send
     */
    public void send_text(InetAddress IP_dest, String txt) {
        Text msg = new Text(username, txt);
        send(msg, IP_dest);
     }
    
}
