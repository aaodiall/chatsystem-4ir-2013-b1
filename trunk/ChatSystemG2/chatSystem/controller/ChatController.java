package chatSystem.controller;

public class ChatController extends Controller implements GuiToCont, NiToCont{
	
    @Override
    public void performConnect() {
                
    }

    @Override
    public void performDisconnect() {
            
    }
        

    @Override
    public void performHelloReceived(boolean ack) {}

    @Override
    public void GoodbyeReceived(String idRemoteSystem) {}
	

}
