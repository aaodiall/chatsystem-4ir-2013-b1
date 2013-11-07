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
        } catch (SocketException exc) {
            System.out.println("Problème à la création du socket d'envoi de messages");
        }
    }
    
    public void sendHello(String username, InetAddress ip, boolean ack, int port) {
        Hello helloToSend = new Hello(username, ack);
        byte[] buffer = helloToSend.toArray();
        DatagramPacket helloMessage = new DatagramPacket(buffer, buffer.length, ip, port);
        try {
            this.messageSocket.send(helloMessage);
        } catch(IOException exc) {
           System.out.println("Problème à l'envoi d'un message");
        } 
    }

}