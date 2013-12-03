package chatsystemg5.network;
import java.net.InetAddress;

/**
 *
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
