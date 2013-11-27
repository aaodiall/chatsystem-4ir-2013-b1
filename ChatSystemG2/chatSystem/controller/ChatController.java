package chatSystem.controller;

import chatSystem.model.FileReceivingInformation;
import chatSystem.model.FileSendingInformation;
import chatSystem.model.FileState;
import chatSystem.model.FileTransfertInformation;
import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;
import chatSystem.view.gui.ChatGUI;
import chatSystem.view.ni.ChatNI;
import java.net.InetAddress;
import java.net.UnknownHostException;
import chatSystem.model.FileTransferts;
import chatSystem.model.RemoteSystemInformation;
import java.io.File;

public class ChatController extends Controller implements GuiToCont, NiToCont {

    private UserInformation localUser; //mettre dans une HasMap créée dans la classe mère
    private RemoteSystems remoteSystems;
    private FileTransferts fileTransferts;
    private final ChatGUI chatGUI;
    private final ChatNI chatNI;
    private ChatAlive chatAlive;

    
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

    @Override
    public void performConnect(String username) {
        System.out.println("Entering performConnect");
        this.localUser.setUsername(username);
        this.localUser.setUserState(UserState.CONNECTED);
        this.chatAlive = new ChatAlive(this.localUser);
        this.chatAlive.setName("chat alive");
        //this.chatAlive.start();
    }

    @Override
    public void performDisconnect() {
        System.out.println("Entering performDisConnect");
        this.localUser.setUserState(UserState.DISCONNECTED);
    }

    @Override
    public void performHelloReceived(String username, String ip, boolean isAck) {
        System.out.println("Hello received, modifying the model");
        String key = RemoteSystemInformation.generateID(username, ip);
        if (!this.remoteSystems.existsRemoteSystem(key))
        {
            this.remoteSystems.addRemoteSystem(username, ip, isAck);
            this.remoteSystems.getRemoteSystem(RemoteSystemInformation.generateID(username, ip)).addObserver(chatGUI);
        } else {
            this.remoteSystems.addRemoteSystem(username, ip, isAck); 
        }
    }

    @Override
    public void performGoodbyeReceived(String idRemoteSystem) {
        System.out.println("Goodbye received, modifying the model");
        this.remoteSystems.deleteRemoteSystem(idRemoteSystem);
    }

    @Override
    public void performMessageReceived(String msg, String idRemoteSystem) {
        System.out.println("Text received, modifying the model");
        this.remoteSystems.addMessageReceivedToRemote(idRemoteSystem, idRemoteSystem + " : " +msg);
    }

    @Override
    public void performSendMessageRequest(String message, String idRemoteSystem) {
        System.out.println("Message to be send to " + idRemoteSystem + ", modifying the model");
        this.remoteSystems.addMessageToSendToRemote(idRemoteSystem, message);
    }

    @Override
    public void performMessageSent(String message, String idRemoteSystem) {
       System.out.println("Message sent to " + idRemoteSystem + ", modifying the model");
       this.remoteSystems.addMessageSentToRemoteSystem(this.localUser.getUsername() + " : " +message, idRemoteSystem);
    }

    @Override
    public void performSendFileRequest(File fileToSend, String idRemoteSystem) {
       System.out.println("Send file request to be send to " + idRemoteSystem + ", modifying the model");
       int idTransfert = this.fileTransferts.addTransfert(fileToSend, idRemoteSystem);
        this.fileTransferts.getFileTransfertInformation(idTransfert).addObserver(chatGUI);
        this.fileTransferts.getFileTransfertInformation(idTransfert).addObserver(chatNI);
        this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.WAITANSWER);
    }
    
    @Override
    public void performAcceptSuggestion(int idTransfert) {
        System.out.println("Send file accepted notification, modifying the model");
        this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.ACCEPTED);
    }
	
    @Override
    public void performDeclineSuggestion(int idTransfert) {
        System.out.println("Send file declined notification, modifying the model");
        this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.DECLINED);
    }
    
    @Override
    public void performSuggestionReceived(String name, long size, String idRemoteSystem, int idTransfert, int portServer) {
        System.out.println("Receiving a file transfert request from " + idRemoteSystem + ", modifying the model");
        this.fileTransferts.addTransfert(idTransfert, name, size, idRemoteSystem, portServer);
        this.fileTransferts.getFileTransfertInformation(idTransfert).addObserver(chatGUI);
        this.fileTransferts.getFileTransfertInformation(idTransfert).addObserver(chatNI);
        this.fileTransferts.getFileTransfertInformation(idTransfert).setState(FileState.WAITANSWER);
    }
    
    @Override
    public void performSaveFile(File fileToSave, int idTransfert) {
        ((FileReceivingInformation)this.fileTransferts.getFileTransfertInformation(idTransfert)).setFileDescriptor(fileToSave);
    }

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
     *
     * @param idTransfert
     * @param filePart
     * @param isLast
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
    
    @Override
    public void performFileSended(int idTransfert, String idRemoteSystem) {
        FileSendingInformation tmp = (FileSendingInformation)this.fileTransferts.getFileTransfertInformation(idTransfert);
        this.remoteSystems.addMessageSentToRemoteSystem("The file " + tmp.getName() + " has been sent to " + tmp.getIdRemoteSystem(), tmp.getIdRemoteSystem());
        tmp.deleteObservers();
        this.fileTransferts.deleteTransfert(idTransfert);
    }

}
