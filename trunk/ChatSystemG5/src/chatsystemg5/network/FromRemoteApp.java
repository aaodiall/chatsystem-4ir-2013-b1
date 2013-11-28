package chatsystemg5.network;
import java.net.InetAddress;

public interface FromRemoteApp {

    public void send_to_controller (byte[] array, InetAddress IP_addr);
    
}
