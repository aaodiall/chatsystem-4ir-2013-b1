package chatSystem.view.ni;

import java.net.DatagramPacket;
import java.net.*;
import chatSystemCommon.*;
import java.io.IOException;

public class MessageTransfert {
    
    private DatagramSocket messageSocket;
    public MessageTransfert() {
        try{
            this.messageSocket = new DatagramSocket();
            this.messageSocket.setBroadcast(true);
        } catch (SocketException exc) {
            System.out.println("Problème à la création du socket d'envoi de messages");
        }
    }
    
    public void sendHello(String username, InetAddress ip, boolean ack, int port) {
        Hello helloToSend = new Hello(username, ack);
        try {
            byte[] buffer = helloToSend.toArray();
            DatagramPacket helloMessage = new DatagramPacket(buffer, buffer.length, ip, port);
            this.messageSocket.send(helloMessage);
        } catch (IOException exc) {
            System.out.println("Probleme à la conversion du message hello ou à l'envoi du message");
        } 
    }
}