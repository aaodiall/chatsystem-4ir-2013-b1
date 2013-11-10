package chatSystem.view.ni;

import java.net.*;
import java.io.IOException;
import java.lang.Runnable;
import chatSystemCommon.*;

//import chatSystem.controller.Controller;

/**
 * Active class receiving messages 
 * @author Marjorie
 */
public class MessageReceiver implements Runnable {
    
    private DatagramSocket serverSocket;
    private byte[] messageReceived;
    private ChatNI chatni;
    
    /**
     * Class' constructor
     * @param serverPort port used for the communication
     * @param tailleMax maximum receiving message's size
     */
    public MessageReceiver(int serverPort, int tailleMax, ChatNI chatni) {
        try {
            this.serverSocket = new DatagramSocket(serverPort);
            this.messageReceived = new byte[tailleMax];
            this.chatni = chatni;
        } catch (SocketException exc) {
            System.out.println("Probleme à la création du socket server");
        }
    }
    
    public void ReceiveHello() {
        while(true) {
            DatagramPacket helloM = new DatagramPacket(this.messageReceived, this.messageReceived.length);
            try {
                this.serverSocket.receive(helloM);
                Hello msg = (Hello)(Message.fromArray(helloM.getData()));
                this.chatni.helloReceived(msg, helloM.getAddress().toString());
            } catch(IOException exc) {
                System.out.println("probleme à la réception d'un message");
            }
        }
    }
    
    /**
     * Action done by the active class
     */
    public void run() {
        ReceiveHello();
    }
}