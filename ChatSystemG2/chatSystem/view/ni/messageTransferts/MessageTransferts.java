package chatSystem.view.ni.messageTransferts;

import chatSystem.model.RemoteSystems;
import chatSystem.model.UserState;
import chatSystem.view.ni.ChatNI;
import chatSystemCommon.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class MessageTransferts implements Runnable {
    
    final static int portUdpEmission = 16001;

    private DatagramSocket messageSocket;
    private RemoteSystems rmInstance;
    private ChatNI chatni;

    //tasks' file
    private FileWithPriority<Task> fileTask;

    /**
     * Class' constructor
     * @param chatni reference to the instance of the chat ni 
     */
    public MessageTransferts(ChatNI chatni) {
        try {
            this.messageSocket = new DatagramSocket();
            this.messageSocket.setBroadcast(true);
            this.rmInstance = RemoteSystems.getInstance();
            this.chatni = chatni;
            this.fileTask = new FileWithPriority<Task>();
        } catch (SocketException exc) {
            System.out.println("Problème à la création du socket d'envoi de messages");
        }
    }

    /**
     * Determine the computer local red's broadcast address.
     * @return broadcast address determined
     */
    private InetAddress determineBroadcastAddress() {
        // boolean found = false;
        InetAddress broadcast = null;
        try {
            broadcast = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageTransferts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return broadcast;
    }

    /**
     * Send a hello message to all the computers located in the local red Using
     * the broadcast address which has to be determined
     */
    protected void sendHello() {
        //System.out.println("Sending hellos");
        //hello sent to everyone can only be a hello without ack
        Hello helloToSend = new Hello(this.chatni.getUserInfo().getUsername(), false);
        InetAddress broadcastAddress = this.determineBroadcastAddress();
        this.sendPacket(broadcastAddress, helloToSend);
    }

    /**
     * Send a hello message to a determined remote system
     * @param ip ip address of the remote system we want to send the message
     */
    protected void sendHello(String ip) {
        //hello sent to one person can only be an ack hello
        Hello helloToSend = new Hello(this.chatni.getUserInfo().getUsername(), true);
        this.sendPacket(ip, helloToSend);
    }

    /**
     * Send a Goodbye message to all the computers located in the local red
     * Using the broadcast address which has to be determined
     */
    protected void sendGoodbye() {
        InetAddress broadcastAddress = this.determineBroadcastAddress();
        Goodbye goodbyeToSend = new Goodbye(this.chatni.getUserInfo().getUsername());
        this.sendPacket(broadcastAddress, goodbyeToSend);
    }

    /**
     * Send a text message to someone and indicates the chatNI the text message has been sent
     * @param idRemoteSystem id of the remote system the text message is to be sent to
     * @param text message content
     */
    protected void sendTextMessage(String idRemoteSystem, String text) {
        String ip = this.rmInstance.getRemoteSystem(idRemoteSystem).getIP();
        Text textToSend = new Text(this.chatni.getUserInfo().getUsername(), text);
        this.sendPacket(ip, textToSend);
        chatni.messageSent(text, idRemoteSystem);
    }

    /**
     * Send a request in order to send a file to a remote system
     * @param name file's name
     * @param size file's size
     * @param idRemoteSystem remote system id
     * @param portClient port used for the transfert
     * @param idTransfert id of the transfert concerned
     */
    protected void sendFileTransfertDemand(String name, long size, String idRemoteSystem, int portClient, int idTransfert) {
        String ip = this.rmInstance.getRemoteSystem(idRemoteSystem).getIP();
        FileTransfertDemand ftd = new FileTransfertDemand(this.chatni.getUserInfo().getUsername(), name, size, portClient,idTransfert);
        this.sendPacket(ip, ftd);
    }

    /**
     * Send a confirmation to a remote system which sent a file transfert request
     * @param isAccepted boolean indicating if the request was accepted or refused
     * @param idTransfertRequest file transfert's id
     * @param idRemoteSystem remote system id
     */
    protected void sendFileTransfertConfirmation(boolean isAccepted, String idRemoteSystem, int idTransfertRequest) {
        String ip = this.rmInstance.getRemoteSystem(idRemoteSystem).getIP();
        FileTransfertConfirmation ftc = new FileTransfertConfirmation(this.chatni.getUserInfo().getUsername(), isAccepted, idTransfertRequest);
        this.sendPacket(ip, ftc);
    }

    /**
     * Private function used to send a Message to someone
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
     * @param ip ip address of the remote system we want to send the message
     * @param msg Message already built we want to send
     */
    private synchronized void sendPacket(InetAddress ip, Message msg) {
        try {
            byte[] buffer = msg.toArray();
            DatagramPacket msgToSend = new DatagramPacket(buffer, buffer.length, ip, MessageTransferts.portUdpEmission);
            System.out.println("ENVOI : " + msg.toString() + " -> " + ip.getHostAddress());
            this.messageSocket.send(msgToSend);
        } catch (IOException exc) {
            System.out.println("Probleme à la conversion du message ou à l'envoi du message");
        }
    }

    /**
     * Set a new sendHelloTask to fulfill
     * the hello message is to be sent to a single remote system
     * @param ip ip adress of the remote system the hello message is to be sent
     */
    public void setHelloTask(String ip) {
        System.out.println("Hello Task added");
        this.fileTask.addUrgentTask(new sendHelloTask(this,ip));
    }

    /**
     * Set a new sendHelloTask to fulfill
     * the hello message is to be sent to all the remote systems
     */
    public void setHelloTask() {
        System.out.println("Hello Task added");
        this.fileTask.addUrgentTask(new sendHelloTask(this));
    }

    /**
     * Set a new sendGoodbyeTask to fulfill
     * the goodbye message is to be sent to all the remote systems
     */
    public void setGoodbyeTask() {
        this.fileTask.addUrgentTask(new sendGoodbyeTask(this));
    }

    /**
     * Set a new sendTextMessageTask to fulfill
     * @param idRemoteSystem id of the remote system the text message is to be sent
     * @param text text's content
     */
    public void setTextMessageTask(String idRemoteSystem,String text) {
        this.fileTask.addTask(new sendTextTask(this, idRemoteSystem,text));
    }

    /**
     * Set a new sendFileDemandTask to fulfill
     * @param name file's name
     * @param size file's size
     * @param idRemoteSystem id of the remote system the request is to be sent
     * @param portClient port the transfert is going to use
     * @param idTransfert id of the transfert concerned
     */
    public void setFileDemandTask(String name, long size, String idRemoteSystem, int portClient, int idTransfert) {
        this.fileTask.addTask(new sendFileTransfertDemandTask(this, name, size, idRemoteSystem, portClient, idTransfert));
    }

    /**
     * Set a new sendFileConfirmationTask to fulfill
     * @param idRemoteSystem id of the remote system the confirmation is to be sent to
     * @param isAccepted boolean indicating whether or not the request has been accepted
     * @param idTransfertRequest id of the transfert request the user is answering 
     */
    public void setFileConfirmationTask(String idRemoteSystem, boolean isAccepted, int idTransfertRequest) {
        this.fileTask.addTask(new sendFileTransfertConfirmationTask(this,idRemoteSystem,isAccepted,idTransfertRequest));
    }

    /**
     *  Wait a new task to do, getNextTask func is using wait
     */
    @Override
    public synchronized void run() {
        while (this.chatni.getUserInfo().getUserState() == UserState.CONNECTED) {
            this.fileTask.getNextTask().execute();
        }
    }


    




  


}
