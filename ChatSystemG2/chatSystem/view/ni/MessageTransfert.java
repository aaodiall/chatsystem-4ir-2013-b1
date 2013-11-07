package chatSystem.view.ni;

import java.net.DatagramPacket;
import java.net.*;
import chatSystemCommon.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Iterator;

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
    
    public InetAddress determineBroadcastAddress(InetAddress ip) {
        boolean found = false;
        InetAddress broadcast = null;
        try {
            Enumeration<NetworkInterface> interfaces =NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements() && !found)  {
                NetworkInterface ni = interfaces.nextElement();
                if (!ni.isLoopback()) {
                    List<InterfaceAddress> addresses = ni.getInterfaceAddresses();
                    Iterator<InterfaceAddress> it = addresses.iterator();
                    while (it.hasNext() && !found) {
                        broadcast = it.next().getBroadcast();
                        if (broadcast != null)
                            found = true;
                    }
                }
            }
        } catch (SocketException exc) {
            System.out.println("Cette machine n'a pas d'interface");
        }
        return broadcast;
    }
    
    //public void sendHello(String username, int port) {}
    
    public void sendHello(String username, InetAddress ip, int port) {
        Hello helloToSend = new Hello(username, false);
        try {
            byte[] buffer = helloToSend.toArray();
            DatagramPacket helloMessage = new DatagramPacket(buffer, buffer.length, ip, port);
            this.messageSocket.send(helloMessage);
        } catch (IOException exc) {
            System.out.println("Probleme à la conversion du message hello ou à l'envoi du message");
        } 
    }
}