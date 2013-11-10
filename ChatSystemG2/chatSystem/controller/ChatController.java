package chatSystem.controller;

import chatSystem.model.UserState;
import chatSystemCommon.*;

public class ChatController extends Controller implements GuiToCont, NiToCont{
	
    @Override
    public void performConnect(String username) {
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
    public void GoodbyeReceived(String idRemoteSystem) {}
	

}
