package chatsystemg5.network;
import java.net.InetAddress;

/**
 * This interface lists the signals from the network to the chat system
 * @author belliot
 */
public interface FromRemoteApp {

    /**
     * This signal is used for the receiving of messages from the network with IP address of the sender
     * @param msg : message received
     * @param IP_addr : sender's IP address
     */
    public void receive_from_network (byte[] msg, InetAddress IP_addr);
    
}
