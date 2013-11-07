package chatSystem.view.ni;

import java.net.*;
import java.io.IOException;
import java.lang.Runnable;

public class MessageReceiver implements Runnable {
    
    private DatagramSocket serverSocket;
    private byte[] messageReceived;
    
    
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
    
    public void run() {
        ReceiveHello();
    }
}