package chatSystem.view.ni;

import java.net.*;
import java.io.IOException;
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
     * @param chatni chatNI responsible for this messageReceiver instance
     * 
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
    
    /**
     * Receive a message and analyze it 
     * Call for the right chatNI's method
     */
    public void receiveMessage() {
        while(true) {
            DatagramPacket packet = new DatagramPacket(this.messageReceived, this.messageReceived.length);
            try {
                this.serverSocket.receive(packet);
                Message msg = Message.fromArray(packet.getData());
                String from = packet.getAddress().toString();
                Class msgClass = msg.getClass();
                if (msgClass == Hello.class){
                    Hello helloReceived = (Hello)msg;
                    this.chatni.helloReceived(helloReceived, from);
                }
                else if (msgClass == Text.class) {
                    this.chatni.textMessageReceived(msg, from);
                }
                else if (msgClass == Goodbye.class) {
                    Goodbye gbReceived = (Goodbye)msg;
                    this.chatni.goodbyeReceived(gbReceived, from);
                }  
                else if (msgClass == FileTransfertDemand.class) {
                    FileTransfertDemand ftd = (FileTransfertDemand) msg;
                    this.chatni.fileTransfertDemandReceived(ftd, from);
                }
                else if (msgClass == FileTransfertConfirmation.class) {
                   FileTransfertConfirmation ftc = (FileTransfertConfirmation) msg;
                   this.chatni.fileTransfetConfirmationReceived(ftc, from);
                }
            } catch(IOException exc) {
                System.out.println("probleme à la réception d'un message");
            }
        }
    }
    
    /**
     * Action done by the active class
     */
    public void run() {
        receiveMessage();
    }
}