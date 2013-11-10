package chatSystem.view.ni;

import chatSystem.view.gui.View;
import chatSystem.controller.ChatController;
import java.util.Observable;
import chatSystemCommon.*;

public class ChatNI extends View implements Runnable {

    private MessageReceiver messageReceiver;
    private MessageTransfert messageTransfert;
    private FileReceiver[] fileReceiver;
    private FileTransfert[] fileTransfert;
    private int numPort;

    public ChatNI(ChatController controller, int serverPort, int tailleMax, int numPort) {
        super(controller);
        this.numPort = numPort;
        this.fileReceiver = new FileReceiver[5];
        this.fileTransfert = new FileTransfert[5];
        this.messageReceiver = new MessageReceiver(serverPort, tailleMax, this);
        this.messageTransfert = new MessageTransfert();
    }

    public void helloReceived(Hello msg, String ip) {
        ((ChatController) (this.controller)).performHelloReceived(msg, ip);
    }

    public void textMessageReceived(Message msg, String ip) {

    }

    public void goodbyeReceived(Goodbye msg, String ip) {

    }

    public void fileTransfertDemandReceived(FileTransfertDemand msg, String ip) {

    }

    public void fileTransfetConfirmationReceived(FileTransfertConfirmation msg, String ip) {

    }

    public void sendHelloMsg(String username, String ip) {
        this.messageTransfert.sendHello(username, null, numPort);
    }

    public void sendHelloMsg(String username) {
        this.messageTransfert.sendHello(username, numPort);
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
