package chatSystem.view.ni;

import java.net.*;
import java.io.IOException;
import java.lang.Runnable;

/**
 * Active class receiving messages 
 * @author Marjorie
 */
public class MessageReceiver implements Runnable {
    
    private DatagramSocket serverSocket;
    private byte[] messageReceived;
    
    /**
     * Class' constructor
     * @param serverPort port used for the communication
     * @param tailleMax maximum receiving message's size
     */
    public MessageReceiver(int serverPort, int tailleMax) {
        try {
            this.serverSocket = new DatagramSocket(serverPort);
            this.messageReceived = new byte[tailleMax];
        } catch (SocketException exc) {
            System.out.println("Probleme à la création du socket server");
        }
    }
    
    public void ReceiveHello() {
        while(true) {
            DatagramPacket helloM = new DatagramPacket(this.messageReceived, this.messageReceived.length);
            try {
                this.serverSocket.receive(helloM);
                System.out.println("HELLO RECEIVED FROM" + helloM.getAddress());
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