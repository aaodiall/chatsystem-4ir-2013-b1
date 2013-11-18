package chatsystemg5.network;

import chatSystemCommon.*;

import java.net.InetAddress;

public interface ToRemoteApp {
    
     abstract public void send (Message msg, InetAddress IP_dest);
	
}