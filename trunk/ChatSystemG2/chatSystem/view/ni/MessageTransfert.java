package chatSystem.view.ni;

import java.net.DatagramPacket;
import java.net.*;
import chatSystemCommon.*;
import java.io.IOException;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Iterator;
import chatSystem.model.RemoteSystems;
import chatSystem.model.RemoteSystemInformation;
//import chatSystem.model.UserInformation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to send messages
 *
 * @author Marjorie
 */
public class MessageTransfert implements Runnable {

    final static int portUdpEmission = 16001;

    private DatagramSocket messageSocket;
    private RemoteSystems rmInstance;
    private ChatNI chatni;

    /**
     * Class' constructor
     * @param chatni inst
     */
    public MessageTransfert(ChatNI chatni) {
        try {
            this.messageSocket = new DatagramSocket();
            this.messageSocket.setBroadcast(true);
            this.rmInstance = RemoteSystems.getInstance();
            this.chatni = chatni;
        } catch (SocketException exc) {
            System.out.println("Problème à la création du socket d'envoi de messages");
        }
    }

    /**
     * Determine the computer local red's broadcast address We assume that the
     * first we find is the rigth one
     *
     * @return broadcast address determined
     */
    public InetAddress determineBroadcastAddress() {
        // boolean found = false;
        InetAddress broadcast = null;
        try {
            broadcast = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageTransfert.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*try {
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
         }*/
        return broadcast;
    }

    /**
     * Send a hello message to all the computers located in the local red Using
     * the broadcast address which has to be determined
     */
    public void sendHello() {
        //hello sent to everyone can only be a hello without ack
        Hello helloToSend = new Hello(this.chatni.getUserInfo().getUsername(), false);
        InetAddress broadcastAddress = this.determineBroadcastAddress();
        System.out.println("ENVOI : " + helloToSend.toString() + " -> " + broadcastAddress.getHostAddress());
        this.sendPacket(broadcastAddress, helloToSend);
    }

    /**
     * Send a hello message to a determined remote system
     *
     * @param ip ip address of the remote system we want to send the message
     */
    public void sendHello(String ip) {
        //hello sent to one person can only be an ack hello
        Hello helloToSend = new Hello(this.chatni.getUserInfo().getUsername(), true);
        System.out.println("ENVOI : " + helloToSend.toString() + " -> " + ip);
        this.sendPacket(ip, helloToSend);
    }

    /**
     * Send a goodbye message to all the user's contacts
     *
     * @param ipAddresses list of remote systems' ip address
     */
    public void sendGoodbye(String[] ipAddresses) {
        Goodbye goodbyeToSend = new Goodbye(this.chatni.getUserInfo().getUsername());
        try {
            byte[] buffer = goodbyeToSend.toArray();
            for (String ip : ipAddresses) {
                try {
                    InetAddress ipAddress = InetAddress.getByName(ip);
                    DatagramPacket goodbyeMessage = new DatagramPacket(buffer, buffer.length, ipAddress, MessageTransfert.portUdpEmission);
                    this.messageSocket.send(goodbyeMessage);
                } catch (UnknownHostException e) {
                    System.err.println("Erreur d'adresse ip");
                }
            }
        } catch (IOException e) {
            System.out.println("Probleme à la conversion du message hello ou à l'envoi du message");
        }
    }

    /**
     * Send a Goodbye message to all the computers located in the local red
     * Using the broadcast address which has to be determined
     */
    public void sendGoodbye() {
        InetAddress broadcastAddress = this.determineBroadcastAddress();
        Goodbye goodbyeToSend = new Goodbye(this.chatni.getUserInfo().getUsername());
        System.out.println("ENVOI : " + goodbyeToSend.toString() + " -> " + broadcastAddress.getHostAddress());
        this.sendPacket(broadcastAddress, goodbyeToSend);
    }

    /**
     * Send a text message to someone
     *
     * @param ip ip address of the remote system we want to send the message
     * @param text message content
     */
    public void sendTextMessage(String ip, String text) {
        Text textToSend = new Text(this.chatni.getUserInfo().getUsername(), text);
        this.sendPacket(ip, textToSend);
    }

    /**
     * Private function used to send a Message to someone
     *
     * @param ip ip address of the remote system we want to send the message
     * @param msg Message already built we want to send
     */
    private void sendPacket(String ip, Message msg) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            this.sendPacket(ipAddress, msg);
        } catch (UnknownHostException exc) {
            System.err.println("probleme a la creation de l'adresse");
            exc.printStackTrace();
        }
    }

    /**
     * Private function used to send a Message
     *
     * @param username username of the person who wants to send the message
     * @param ip ip address of the remote system we want to send the message
     */
    private synchronized void sendPacket(InetAddress ip, Message msg) {
        try {
            byte[] buffer = msg.toArray();
            DatagramPacket msgToSend = new DatagramPacket(buffer, buffer.length, ip, MessageTransfert.portUdpEmission);
            this.messageSocket.send(msgToSend);
        } catch (IOException exc) {
            System.out.println("Probleme à la conversion du message ou à l'envoi du message");
        }
    }
    
    @Override
    public void run() {
        
        String msgToSend = null;
        
        while(true) {
            //fonction bloquante permettant d'attendre qu'un message à envoyer soit disponible
            this.rmInstance.waitMessageToSend();
            //parcours de tous les RemoteSystemInformation
            for (RemoteSystemInformation rsInfo : this.rmInstance) {
                //tant qu'il reste des messages à envoyer
                do {
                    //récupération du message à envoyer
                    msgToSend = rsInfo.getMessageToSend();
                    //envoi du message
                    this.sendTextMessage(rsInfo.getIP(), msgToSend);
                    //notification de chatni
                    chatni.messageSended(msgToSend, rsInfo.getUsername());
                } while (msgToSend != null);
            }
        }
    }
}
