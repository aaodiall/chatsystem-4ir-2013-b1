/**
 * Controls the system' execution
 */


package chatSystem.controller;

import chatSystem.model.*;
import chatSystem.view.gui.ChatGUI;
import chatSystem.view.ni.ChatNI;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.File;

public class ChatController extends Controller implements GuiToCont, NiToCont {

    private UserInformation localUser;
    private RemoteSystems remoteSystems;
    private FileTransferts fileTransferts;
    private final ChatGUI chatGUI;
    private final ChatNI chatNI;
    private ChatAlive chatAlive;

    /**
     * Class' constructor
     */
    public ChatController() {
        super();
        
        InetAddress localIP;
        try {
            localIP = InetAddress.getLocalHost();
            this.localUser = new UserInformation(localIP.getHostAddress());
            this.remoteSystems = RemoteSystems.getInstance();
            this.fileTransferts = FileTransferts.getInstance();
            
        } catch (UnknownHostException ex) {
            System.err.println("local host non existent");
        }
        
        this.chatGUI = new ChatGUI(this);
        this.localUser.addObserver(chatGUI);
        this.remoteSystems.addObserver(chatGUI);

        this.chatNI = new ChatNI(this);
        this.localUser.addObserver(chatNI);
        this.remoteSystems.addObserver(chatNI);
    }

    /**
     * Perform the user's attempt to connect himself
     * @param username username the user chose
     */
    @Override
    public void performConnect(String username) {
        System.out.println("Entering performConnect");
        this.localUser.setUsername(username);
        this.localUser.setUserState(UserState.CONNECTED);
        this.chatAlive = new ChatAlive(this.localUser);
        this.chatAlive.setName("chat alive");
        //this.chatAlive.start();
    }

    /**
     * Perform the user's attempt to get disconnected
     */
    @Override
    public void performDisconnect() {
        System.out.println("Entering performDisConnect");
        this.localUser.setUserState(UserState.DISCONNECTED);
    }
    
    /**
     * Send a text message to a given remote system
     * @param message text message' content
     * @param idRemoteSystem id of the remote system the text message is to be sent to
     */
    @Override
    public void performSendMessageRequest(String message, String idRemoteSystem) {
        System.out.println("Message to be send to " + idRemoteSystem + ", modifying the model");
        this.remoteSystems.addMessageToSendToRemote(idRemoteSystem, message);
    }
    
     /**
     * Perform the sending of a file transfert deman to a given remote system
     * @param fileToSend file the user wants to send
     * @param idRemoteSystem id of the remote system the request is to be sent to
     */
    @Override
    public void performSendFileRequest(File fileToSend, String idRemoteSystem) {
       System.out.println("Send file request to be send to " + idRemoteSystem + ", modifying the model");
       int idTransfert = this.fileTransferts.addTransfert(fileToSend, idRemoteSystem);
        this.fileTransferts.getFileTransfertInformation(idTransfert).addObserver(chatGUI);
        this.fileTransferts.getFileTransfertInformation(idTransfert).addObserver(chatNI);
        this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.WAITANSWER);
    }
    
    /**
     * Perform the consent of a file transfert demande by the local user
     * @param idTransfert id of the transfert which was accepted
     */
    @Override
    public void performAcceptSuggestion(int idTransfert) {
        System.out.println("Send file accepted notification, modifying the model");
        this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.ACCEPTED);
    }
	
     /**
     * Perform the refusal of a file transfert demande by the local user
     * @param idTransfert id of the transfert which was refused
     */
    @Override
    public void performDeclineSuggestion(int idTransfert) {
        System.out.println("Send file declined notification, modifying the model");
        this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.DECLINED);
    }
    
    /**
     * Perform the copy of the file
     * @param fileToSave file which is to be saved
     * @param idTransfert transfert's id
     */
    @Override
    public void performSaveFile(File fileToSave, int idTransfert) {
        ((FileReceivingInformation)this.fileTransferts.getFileTransfertInformation(idTransfert)).setFileDescriptor(fileToSave);
    }
    
    /**
     * React after the reception of a hello message 
     * @param username contact who sent the hello message
     * @param ip contact's ip
     * @param isAck whether or not the hello message is a answer
     */
    @Override
    public void performHelloReceived(String username, String ip, boolean isAck) {
        System.out.println("Hello received, modifying the model");
        this.remoteSystems.addRemoteSystem(username, ip, isAck);
        this.remoteSystems.getRemoteSystem(RemoteSystemInformation.generateID(username, ip)).addObserver(chatGUI);
    }

    /**
     * React after the reception of a goodbye message
     * @param idRemoteSystem id of the remote system which sent the goodbye message
     */
    @Override
    public void performGoodbyeReceived(String idRemoteSystem) {
        System.out.println("Goodbye received, modifying the model");
        this.remoteSystems.deleteRemoteSystem(idRemoteSystem);
    }

    /**
     * React after the reception of a text message
     * @param msg text message's content
     * @param idRemoteSystem id of the remote system which sent the text message
     */
    @Override
    public void performMessageReceived(String msg, String idRemoteSystem) {
        System.out.println("Text received, modifying the model");
        this.remoteSystems.addMessageReceivedToRemote(idRemoteSystem, idRemoteSystem + " : " +msg);
    }

    /**
     * React after having sent a text message to a given remote system
     * @param message text message' content
     * @param idRemoteSystem id of the remote system the text message has been sent to
     */
    @Override
    public void performMessageSent(String message, String idRemoteSystem) {
       System.out.println("Message sent to " + idRemoteSystem + ", modifying the model");
       this.remoteSystems.addMessageSentToRemoteSystem(this.localUser.getUsername() + " : " +message, idRemoteSystem);
    }
  
    /**
     * React after the reception of a file transfert demand
     * @param name file's name
     * @param size file's size
     * @param idRemoteSystem id of the remote system which sent the request
     * @param idTransfert transfert's id
     * @param portServer port which is going to be used for the transfert
     */
    @Override
    public void performSuggestionReceived(String name, long size, String idRemoteSystem, int idTransfert, int portServer) {
        System.out.println("Receiving a file transfert request from " + idRemoteSystem + ", modifying the model");
        this.fileTransferts.addTransfert(idTransfert, name, size, idRemoteSystem, portServer);
        this.fileTransferts.getFileTransfertInformation(idTransfert).addObserver(chatGUI);
        this.fileTransferts.getFileTransfertInformation(idTransfert).addObserver(chatNI);
        this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.WAITANSWER);
    }
    
    /**
     * React after the reception of file transfert's confirmation
     * @param idRemoteSystem id of the remote system which confirmed the file's transfert
     * @param idTransfert file transfert's id
     * @param accepted whether the transfert was accepted or declined
     */
    @Override
    public void performConfirmationReceived(String idRemoteSystem, int idTransfert, boolean accepted) {
        System.out.println("Receiving a file transfert confirmation from " + idRemoteSystem + ", modifying the model");
        if (accepted) {
            FileTransfertInformation tmp = this.fileTransferts.getFileTransfertInformation(idTransfert);
            this.remoteSystems.addMessageSentToRemoteSystem("The file " + tmp.getName() + " was accepted by " + tmp.getIdRemoteSystem(), tmp.getIdRemoteSystem());
            this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.ACCEPTED);
        } else {
            FileTransfertInformation tmp = this.fileTransferts.getFileTransfertInformation(idTransfert);
            this.remoteSystems.addMessageSentToRemoteSystem("the file " + tmp.getName() + " was rejected by " + tmp.getIdRemoteSystem(), tmp.getIdRemoteSystem());
            this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.DECLINED);
        }
    }
    
    /**
     * React after the reception of part of a file during a transfert
     * @param idTransfert file transfert's id
     * @param filePart file's part that was received
     * @param isLast whether or not it is the file's last part
     */
    @Override
    public void performFilePartReceived(int idTransfert, byte[] filePart, boolean isLast){
        FileReceivingInformation tmp = (FileReceivingInformation)this.fileTransferts.getFileTransfertInformation(idTransfert);
        tmp.addFilePart(filePart);
        tmp.setIsLast(isLast);
        if(isLast){
            this.remoteSystems.addMessageSentToRemoteSystem("The file " + tmp.getName() + " from " + tmp.getIdRemoteSystem() + " has been received", tmp.getIdRemoteSystem());
            this.fileTransferts.getFileTransfertInformation(idTransfert).deleteObservers();
            this.fileTransferts.deleteTransfert(idTransfert);
        }
    }
    
    /**
     * React after having terminated a file's transfert
     * @param idTransfert file transfert's id
     * @param idRemoteSystem id of the remote system the file has been sent to
     */
    @Override
    public void performFileSended(int idTransfert, String idRemoteSystem) {
        FileSendingInformation tmp = (FileSendingInformation)this.fileTransferts.getFileTransfertInformation(idTransfert);
        this.remoteSystems.addMessageSentToRemoteSystem("The file " + tmp.getName() + " has been sent to " + tmp.getIdRemoteSystem(), tmp.getIdRemoteSystem());
        tmp.deleteObservers();
        this.fileTransferts.deleteTransfert(idTransfert);
    }

    /**
     * Ask the GUI to display a notification to inform the user that a problem occurs during the connection
     */
    @Override
    public void performConnectionError() {
        this.chatGUI.displayConnectionErrorNotification();
    }
    
    /**
     * Ask the GUI to display a notification to inform the user that a problem occurs during a file Transfert
     * @param idTransfert the id of the transfert where error occurs
     */
    @Override
    public void performFileTransfertError(int idTransfert){
        FileTransfertInformation tmp = this.fileTransferts.getFileTransfertInformation(idTransfert);
        this.chatGUI.displayFileTransfertErrorNotification(tmp.getIdRemoteSystem(), tmp.getName());
        tmp.setState(FileState.TERMINATED);
        this.remoteSystems.addMessageSentToRemoteSystem("An error occurs during the transfert of " + tmp.getName(),tmp.getIdRemoteSystem());
        tmp.deleteObservers();
        this.fileTransferts.deleteTransfert(idTransfert);
    }
}
