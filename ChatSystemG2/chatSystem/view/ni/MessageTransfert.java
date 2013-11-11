package chatSystem.view.ni;

import java.net.DatagramPacket;
import java.net.*;
import chatSystemCommon.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Iterator;


/**
 * Class used to send messages
 * @author Marjorie
 */
public class MessageTransfert {
    
    final static int portUdpEmission = 16000;
    
    private DatagramSocket messageSocket;
    /**
     * Class' constructor
     */
    public MessageTransfert() {
        try{
            this.messageSocket = new DatagramSocket();
            this.messageSocket.setBroadcast(true);
        } catch (SocketException exc) {
            System.out.println("Problème à la création du socket d'envoi de messages");
        }
    }
    
    /**
     * Determine the computer local red's broadcast address
     * We assume that the first we find is the rigth one
     * @return broadcast address determined
     */
    public InetAddress determineBroadcastAddress() {
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
    
    /**
     * Send a hello message to all the computers located in the local red
     * Using the broadcast address which has to be determined
     * @param username username of the person who wants to send the message
     */
    public void sendHello(String username) {
        //hello sent to everyone can only be a hello without ack
        Hello helloToSend = new Hello(username, false);
        InetAddress broadcastAddress = this.determineBroadcastAddress();
        this.sendPacket(broadcastAddress, helloToSend);
    }
    
    /**
     * Send a hello message to a determined remote system
     * @param username username of the person who wants to send the message
     * @param ip ip address of the remote system we want to send the message
     */
    public void sendHello(String username, String ip) {
        //hello sent to one person can only be an ack hello
        Hello helloToSend = new Hello(username, true);
        this.sendPacket(ip, helloToSend);
    }
    
    /**
     * Send a goodbye message to all the user's contacts
     * @param username username of the person who wants to send the message
     * @param ipAddresses list of remote systems' ip address
     */
    public void sendGoodbye(String username, String[] ipAddresses) {
        Goodbye goodbyeToSend = new Goodbye(username);
        try {
            byte[] buffer = goodbyeToSend.toArray();
            for (String ip: ipAddresses) {
                try {
                   InetAddress ipAddress = InetAddress.getByName(ip);
                   DatagramPacket goodbyeMessage = new DatagramPacket(buffer, buffer.length, ipAddress, MessageTransfert.portUdpEmission);
                   this.messageSocket.send(goodbyeMessage);
                } catch(UnknownHostException e) {
                    System.err.println("Erreur d'adresse ip");
                }
            }
        }catch(IOException e) {
            System.out.println("Probleme à la conversion du message hello ou à l'envoi du message");
        }         
    }
    
    /**
     * Send a Goodbye message to all the computers located in the local red
     * Using the broadcast address which has to be determined
     * @param username username of the person who wants to send the message
     */
    public void sendGoodbye(String username) {
        InetAddress broadcastAddress = this.determineBroadcastAddress();
        Goodbye goodbyeToSend = new Goodbye(username);
        this.sendPacket(broadcastAddress, goodbyeToSend);
    }
    
    /**
     * Send a text message to someone
     * @param username username of the person who wants to send the message
     * @param ip ip address of the remote system we want to send the message
     * @param text message content
     */
    public void sendTextMessage(String username, String ip, String text) {
        Text textToSend = new Text(username, text);
        this.sendPacket(ip, textToSend);
    }
    
    /**
     * Private function used to send a Message to someone
     * @param ip ip address of the remote system we want to send the message
     * @param msg Message already built we want to send
     */
    private void sendPacket(String ip, Message msg) {
        try{
            InetAddress ipAddress = InetAddress.getByName(ip);
            this.sendPacket(ipAddress, msg);
        } catch(UnknownHostException exc) {
            System.err.println("probleme a la creation de l'adresse");
        }
    }
    
     /**
     * Private function used to send a Message
     * @param username username of the person who wants to send the message
     * @param ip ip address of the remote system we want to send the message
     */
    private void sendPacket(InetAddress ip, Message msg) {
        try {
            byte[] buffer = msg.toArray();
            DatagramPacket msgToSend = new DatagramPacket(buffer, buffer.length, ip, MessageTransfert.portUdpEmission);
            this.messageSocket.send(msgToSend);
        } catch (IOException exc) {
            System.out.println("Probleme à la conversion du message hello ou à l'envoi du message");
        }
    }
}