package chatsystemg5.network;
import chatsystemg5.common.*;
import java.net.InetAddress;

public interface FromRemoteApp {

    public void send_to_controller (byte[] array, InetAddress IP_addr);
    
}
