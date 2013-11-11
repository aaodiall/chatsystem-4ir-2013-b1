package chatsystemg5.network;

import java.lang.Thread;
import java.net.*;



public class MessageEmissionNI extends MessageHandlerNI implements ToRemoteApp {

    public MessageEmissionNI(Message msg) throws SocketException {
        
        // Créer un datagramme socket
        DatagramSocket data_sock = new DatagramSocket(16000);
        
    }
    
    public void send () {
        
    }
    
    
}
