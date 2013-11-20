package chatSystem.view.ni;

import java.net.DatagramPacket;
import java.net.*;
import chatSystemCommon.*;
import java.io.IOException;
import chatSystem.model.RemoteSystems;
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

    //tasks' file
    private FileWithPriority<Task> fileTask;

    /**
     * Class' constructor
     *
     * @param chatni inst
     */
    public MessageTransfert(ChatNI chatni) {
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
     * Determine the computer local red's broadcast address We assume that the
     * first we find is the rigth one
     *
     * @return broadcast address determined
     */
    private InetAddress determineBroadcastAddress() {
        // boolean found = false;
        InetAddress broadcast = null;
        try {
            broadcast = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageTransfert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return broadcast;
    }

    /**
     * Send a hello message to all the computers located in the local red Using
     * the broadcast address which has to be determined
     */
    private void sendHello() {
        System.out.println("Sending hellos");
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
    private void sendHello(String ip) {
        //hello sent to one person can only be an ack hello
        Hello helloToSend = new Hello(this.chatni.getUserInfo().getUsername(), true);
        System.out.println("ENVOI : " + helloToSend.toString() + " -> " + ip);
        this.sendPacket(ip, helloToSend);
    }

    /**
     * Send a Goodbye message to all the computers located in the local red
     * Using the broadcast address which has to be determined
     */
    private void sendGoodbye() {
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
    private void sendTextMessage(String ip, String text) {
        Text textToSend = new Text(this.chatni.getUserInfo().getUsername(), text);
        this.sendPacket(ip, textToSend);
    }

    /**
     * Send a request in order to send a file to a remote system
     *
     * @param name file's name
     * @param size file's size
     * @param idTransfert file transfert's id
     * @param idRemoteSystem remote system id
     * @param portClient port used for the transfert
     */
    private void sendFileTransfertDemand(String name, long size, int idTransfert, String idRemoteSystem, int portClient) {
        String ip = this.rmInstance.getRemoteSystem(idRemoteSystem).getIP();
        FileTransfertDemand ftd = new FileTransfertDemand(this.chatni.getUserInfo().getUsername(), name, size, portClient);
        this.sendPacket(ip, ftd);
    }

    /**
     * Send a confirmation to a remote system which sent a file transfert
     * request
     *
     * @param isAccepted boolean indicating if the request was accepted or
     * refused
     * @param idTransfert file transfert's id
     * @param idRemoteSystem remote system id
     */
    private void sendFileTransfertConfirmation(boolean isAccepted, int idTransfert, String idRemoteSystem) {
        String ip = this.rmInstance.getRemoteSystem(idRemoteSystem).getIP();
        FileTransfertConfirmation ftc = new FileTransfertConfirmation(this.chatni.getUserInfo().getUsername(), isAccepted, idTransfert);
        this.sendPacket(ip, ftc);
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
     * @param ip ip address of the remote system we want to send the message
     * @param msg Message already built we want to send
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

    public void setHelloTask(String ip) {
        System.out.println("Hello Task added");
        this.fileTask.addUrgentTask(new sendHelloTask(ip));
    }

    public void setHelloTask() {
        System.out.println("Hello Task added");
        this.fileTask.addUrgentTask(new sendHelloTask());
    }

    public void setGoodbyeTask() {
        this.fileTask.addUrgentTask(new sendGoodbyeTask());
    }

    public void setTextMessageTask(String ip, String idRemoteSystem,String text) {
        this.fileTask.addTask(new sendTextTask(ip, idRemoteSystem,text));
    }

    public void setFileDemandTask(String name, long size, int idTransfert, String idRemoteSystem, int portClient) {
        this.fileTask.addTask(new sendFileTransfertDemandTask(name, size, idTransfert, idRemoteSystem, portClient));
    }

    public void setFileConfirmationTask(int idTransfert, String idRemoteSystem, boolean isAccepted) {
        this.fileTask.addTask(new sendFileTransfertConfirmationTask(idTransfert, idRemoteSystem, isAccepted));
    }

    /**
     *  Wait a new task to do, getNextTask func is using wait
     */
    @Override
    public synchronized void run() {
        while (true) {
            this.fileTask.getNextTask().execute();
        }
    }

    private abstract class Task {

        public abstract void execute();
    }

    private class sendHelloTask extends Task {

        private final String ip;

        public sendHelloTask(String ip) {
            this.ip = ip;
        }

        public sendHelloTask() {
            this.ip = null;
        }

        @Override
        public void execute() {
            if (ip == null) {
                sendHello();
            } else {
                sendHello(ip);
            }
        }
    }

    private class sendGoodbyeTask extends Task {

        public sendGoodbyeTask() {
        }

        @Override
        public void execute() {
            sendGoodbye();
        }
    }

    private class sendTextTask extends Task {

        private final String ip;
        private final String text;
        private final String idRemoteSystem;

        public sendTextTask(String ip, String idRemoteSystem,String text) {
            this.ip = ip;
            this.text = text;
            this.idRemoteSystem = idRemoteSystem;
        }

        @Override
        public void execute() {
            sendTextMessage(ip, text);
            chatni.messageSent(text, idRemoteSystem);
        }
    }

    private class sendFileTransfertDemandTask extends Task {

        private final String name;
        private final long size;
        private final int idTransfert;
        private final String idRemoteSystem;
        private final int portClient;

        public sendFileTransfertDemandTask(String name, long size, int idTransfert, String idRemoteSystem, int portClient) {
            this.name = name;
            this.size = size;
            this.idTransfert = idTransfert;
            this.idRemoteSystem = idRemoteSystem;
            this.portClient = portClient;
        }

        @Override
        public void execute() {
            sendFileTransfertDemand(name, size, idTransfert, idRemoteSystem, portClient);
        }
    }

    private class sendFileTransfertConfirmationTask extends Task {

        private final int idTransfert;
        private final String idRemoteSystem;
        private final boolean isAccepted;

        public sendFileTransfertConfirmationTask(int idTransfert, String idRemoteSystem, boolean isAccepted) {
            this.idTransfert = idTransfert;
            this.idRemoteSystem = idRemoteSystem;
            this.isAccepted = isAccepted;
        }

        @Override
        public void execute() {
            sendFileTransfertConfirmation(isAccepted, idTransfert, idRemoteSystem);
        }
    }
}
