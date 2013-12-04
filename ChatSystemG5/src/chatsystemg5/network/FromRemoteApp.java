package chatsystemg5.network;
import java.net.InetAddress;

/**
 * This interface lists the signals from the network to the chat system
 * @author belliot
 */
public interface FromRemoteApp {

    /**
     *
     * @param array
     * @param IP_addr
     */
    public void send_to_controller (byte[] array, InetAddress IP_addr);
    
}
