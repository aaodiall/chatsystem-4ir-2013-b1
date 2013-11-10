package chatSystem.controller;

import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;
import chatSystem.view.gui.ChatGUI;
import chatSystemCommon.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ChatController extends Controller implements GuiToCont, NiToCont{
    
    private UserInformation localUser;      ///mettre dans une HasMap créée dans la classe mère
    private RemoteSystems remoteSystems;
    
    private ChatGUI chatGUI;
    
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
    }
	
    @Override
    public void performConnect(String username) {
        System.out.println("Entering performConnect");
        this.localUser.setUsername(username);
        this.localUser.setState(UserState.CONNECTED);
    }

    @Override
    public void performDisconnect() {
            
    }
        

    @Override
    public void performHelloReceived(Hello msg, String ip) {
        this.remoteSystems.addRemoteSystem(msg.getUsername(), ip);
    }

    @Override
    public void performGoodbyeReceived(String idRemoteSystem) {}
	

}
