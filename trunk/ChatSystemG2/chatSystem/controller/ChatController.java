package chatSystem.controller;

import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;
import chatSystem.view.gui.ChatGUI;
import chatSystem.view.ni.ChatNI;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ChatController extends Controller implements GuiToCont, NiToCont{
    
    private UserInformation localUser;      ///mettre dans une HasMap créée dans la classe mère
    private RemoteSystems remoteSystems;
    
    private final ChatGUI chatGUI;
    private final ChatNI chatNI;
    
    public ChatController() {
        super();
        
        InetAddress localIP;
        try {
            localIP = InetAddress.getLocalHost();
            this.localUser = new UserInformation(localIP.toString());
            this.remoteSystems = RemoteSystems.getInstance();
        } catch (UnknownHostException ex) {
            System.out.println("local host non existent");
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
        this.localUser.setState(UserState.CONNECTED);
    }

    @Override
    public void performDisconnect() {
        this.localUser.setState(UserState.DISCONNECTED);
    }
        

    @Override
    public void performHelloReceived(String username, String ip) {
        System.out.println("Hello received je modif le model");
        this.remoteSystems.addRemoteSystem(username, ip);
    }

    @Override
    public void performGoodbyeReceived(String idRemoteSystem) {
        System.out.println("Goodbye received je modif le model");
        this.remoteSystems.deleteRemoteSystem(idRemoteSystem);
    }

    @Override
    public void performMessageReceived(String msg, String idRemoteSystem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void performSendMessageRequest(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	

}
