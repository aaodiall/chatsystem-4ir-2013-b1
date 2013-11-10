package chatSystem.view.ni;

import chatSystem.view.gui.View;
import chatSystem.controller.Controller;
import java.util.Observable;

public class ChatNI extends View {
	private MessageReceiver messageReceiver; 
	private MessageTransfert messageTransfert;
	private FileReceiver[] fileReceiver;
	private FileTransfert[] fileTransfert;
	private int numPort;
        
	public ChatNI(Controller controller, int serverPort, int tailleMax, int numPort) {
            super(controller);
            this.numPort = numPort;
            this.fileReceiver = new FileReceiver[5];
            this.fileTransfert = new FileTransfert[5];
            this.messageReceiver = new MessageReceiver(serverPort, tailleMax);
            this.messageTransfert = new MessageTransfert();
	}

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
	
}
