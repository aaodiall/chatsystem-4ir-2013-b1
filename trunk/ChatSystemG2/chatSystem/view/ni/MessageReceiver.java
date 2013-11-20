package chatSystem.view.ni;

import chatSystem.model.RemoteSystemInformation;
import java.net.*;
import java.io.IOException;
import chatSystemCommon.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//import chatSystem.controller.Controller;
/**
 * Active class receiving messages
 *
 * @author Marjorie
 */
public class MessageReceiver implements Runnable {

    final static int portUdpReception = 16001;
    final static int tailleMaxDatagram = 1024;
    
    final String ipLocal;

    private DatagramSocket serverSocket;
    private byte[] messageReceived;
    private ChatNI chatni;

    /**
     * Class' constructor
     *
     * @param chatni chatNI responsible for this messageReceiver instance
     *
     */
    public MessageReceiver(ChatNI chatni) {
        try {
            this.serverSocket = new DatagramSocket(MessageReceiver.portUdpReception);
            this.messageReceived = new byte[MessageReceiver.tailleMaxDatagram];
            this.chatni = chatni;
        } catch (SocketException exc) {
            System.err.println("Probleme à la création du socket server");
        }
        InetAddress localIP = null;
        try {
            localIP = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (localIP != null)
            this.ipLocal = localIP.getHostAddress();
        else 
            this.ipLocal = null;
    }

    /**
     * Receive a message and analyze it Call for the right chatNI's method
     */
    private void receiveMessage() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(this.messageReceived, this.messageReceived.length);
            try {
                this.serverSocket.receive(packet);
                Message msg = Message.fromArray(packet.getData());
                String from = packet.getAddress().getHostAddress();
                if (!from.equals(this.ipLocal)) {
                    Class msgClass = msg.getClass();
                    System.out.println("RECEPTION : " + msg.toString() + " <- " + from + " je passe à chatNI");
                    if (msgClass == Hello.class) {
                        Hello helloReceived = (Hello) msg;
                        this.chatni.helloReceived(helloReceived.getUsername(), from);
                    } else if (msgClass == Text.class) {
                        Text msgText = (Text) msg;
                        this.chatni.textMessageReceived(msgText.getText(), RemoteSystemInformation.generateID(msg.getUsername(), from));
                    } else if (msgClass == Goodbye.class) {
                        Goodbye gbReceived = (Goodbye) msg;
                        this.chatni.goodbyeReceived(RemoteSystemInformation.generateID(gbReceived.getUsername(),from));
                    } else if (msgClass == FileTransfertDemand.class) {
                        FileTransfertDemand ftd = (FileTransfertDemand) msg;
                        this.chatni.fileTransfertDemandReceived(ftd, from);
                    } else if (msgClass == FileTransfertConfirmation.class) {
                        FileTransfertConfirmation ftc = (FileTransfertConfirmation) msg;
                        this.chatni.fileTransfertConfirmationReceived(ftc, from);
                    }
                }
            } catch (IOException exc) {
                System.err.println("probleme à la réception d'un message");
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
