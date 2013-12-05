package chatsystemg5.network;

import chatSystemCommon.*;

import java.net.InetAddress;

/**
 * This interface lists the signals from the chat system to the network
 * @author belliot
 */
public interface ToRemoteApp {
    
     /**
     * This signal is used for the sending of messages on the network thanks to an IP address
     * @param msg : message to send
     * @param IP_dest : receiver's IP address
     */
    abstract public void send_on_network (byte[] msg, InetAddress IP_dest);
	
}