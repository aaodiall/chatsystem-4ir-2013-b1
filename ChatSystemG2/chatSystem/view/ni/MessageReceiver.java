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
    
    final static int portUdpReception = 16000;
    final static int tailleMaxDatagram = 1024;
    
    private DatagramSocket serverSocket;
    private byte[] messageReceived;
    private ChatNI chatni;
    
    /**
     * Class' constructor
     * @param chatni chatNI responsible for this messageReceiver instance
     * 
     */
    public MessageReceiver(ChatNI chatni) {
        try {
            this.serverSocket = new DatagramSocket(MessageReceiver.portUdpReception);
            this.messageReceived = new byte[MessageReceiver.tailleMaxDatagram];
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
                String from = packet.getAddress().getHostAddress();
                Class msgClass = msg.getClass();
                if (msgClass == Hello.class){
                    System.out.println("Hello received je passe a chatNI");
                    Hello helloReceived = (Hello)msg;
                    this.chatni.helloReceived(helloReceived.getUsername(), from);
                }
                else if (msgClass == Text.class) {
                    this.chatni.textMessageReceived(((Text)msg).getText(),msg.getUsername(), from);
                }
                else if (msgClass == Goodbye.class) {
                    Goodbye gbReceived = (Goodbye)msg;
                    this.chatni.goodbyeReceived(gbReceived.getUsername(), from);
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
    @Override
    public void run() {
        receiveMessage();
    }
}