package chatsystemg5.network;

import chatSystemCommon.*;

import java.net.InetAddress;

/**
 *
 * @author belliot
 */
public interface ToRemoteApp {
    
     /**
     *
     * @param msg
     * @param IP_dest
     */
    abstract public void send (Message msg, InetAddress IP_dest);
	
}