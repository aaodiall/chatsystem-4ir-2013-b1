package chatSystem.controller;

import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;
import chatSystem.view.gui.ChatGUI;
import chatSystem.view.ni.ChatNI;
import chatSystemCommon.FileTransfertDemand;
import java.net.InetAddress;
import java.net.UnknownHostException;
import chatSystem.model.FileTransferts;

public class ChatController extends Controller implements GuiToCont, NiToCont {

    private int transfertID; 
    private UserInformation localUser; //mettre dans une HasMap créée dans la classe mère
    private RemoteSystems remoteSystems;
    private FileTransferts fileTransferts;
    private final ChatGUI chatGUI;
    private final ChatNI chatNI;

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
        
        this.transfertID = 0;
        this.chatGUI = new ChatGUI(this);
        this.localUser.addObserver(chatGUI);
        this.remoteSystems.addObserver(chatGUI);
        this.fileTransferts.addObserver(chatGUI);

        this.chatNI = new ChatNI(this);
        this.localUser.addObserver(chatNI);
        this.remoteSystems.addObserver(chatNI);
        this.fileTransferts.addObserver(chatNI);
    }

    @Override
    public void performConnect(String username) {
        System.out.println("Entering performConnect");
        this.localUser.setUsername(username + "@" + this.localUser.getIP());
        this.localUser.setUserState(UserState.CONNECTED);
    }

    @Override
    public void performDisconnect() {
        this.localUser.setUserState(UserState.DISCONNECTED);
    }

    @Override
    public void performHelloReceived(String username, String ip) {
        System.out.println("Hello received, modifying the model");
        this.remoteSystems.addRemoteSystem(username, ip);
        
        //ajout de remote system Information en tant qu'observable pr chatGUI
        //this.remoteSystems.getRemoteSystem(username+"@"+ip).addObserver(this.chatGUI);
        
    }

    @Override
    public void performGoodbyeReceived(String idRemoteSystem) {
        System.out.println("Goodbye received, modifying the model");
        this.remoteSystems.deleteRemoteSystem(idRemoteSystem);
    }

    @Override
    public void performMessageReceived(String msg, String idRemoteSystem) {
        System.out.println("Text received, modifying the model");
        this.remoteSystems.addMessageReceivedToRemote(idRemoteSystem, msg);
    }

    @Override
    public void performSendMessageRequest(String message, String idRemoteSystem) {
        System.out.println("Message to be send to " + idRemoteSystem + ", modifying the model");
        this.remoteSystems.addMessageToSendToRemote(idRemoteSystem, message);
    }

    @Override
    public void performSentMessageToRemoteSystem(String message, String idRemoteSystem) {
        System.out.println("Message sent to " + idRemoteSystem + ", modifying the model");
       this.remoteSystems.addMessageSentToRemoteSystem(message, idRemoteSystem, this.localUser.getUsername());
    }

    @Override
    public void performSendFileRequest(String name, long size, String idRemoteSystem) {
       System.out.println("Send file request to be send to " + idRemoteSystem + ", modifying the model");
       this.fileTransferts.addTransfert(name, size, idRemoteSystem, this.transfertID);
       this.transfertID ++;
    }

    @Override
    public void performSuggestionReceived(FileTransfertDemand suggestion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
