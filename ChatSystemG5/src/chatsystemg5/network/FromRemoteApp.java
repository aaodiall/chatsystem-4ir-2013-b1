package chatsystemg5.network;
import chatSystemCommon.*;
import java.net.InetAddress;

public interface FromRemoteApp {

    public void send_to_controller (byte[] array, InetAddress IP_addr);
    
}
