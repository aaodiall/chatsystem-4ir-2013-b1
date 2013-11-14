package chatsystemg5.network;

import chatsystemg5.common.*;

import java.net.InetAddress;

public interface ToRemoteApp {
    
     abstract public void send (Message msg, InetAddress IP_dest);
	
}