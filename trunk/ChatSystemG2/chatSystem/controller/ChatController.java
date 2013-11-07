package chatSystem.controller;

import chatSystemCommon.*;

public class ChatController extends Controller implements GuiToCont, NiToCont{
	
    @Override
    public void performConnect() {
              
    }

    @Override
    public void performDisconnect() {
            
    }
        

    @Override
    public void performHelloReceived(Hello msg, String ip) {
        this.remoteSystems.addRemoteSystem(msg.);
    
    }

    @Override
    public void GoodbyeReceived(String idRemoteSystem) {}
	

}
