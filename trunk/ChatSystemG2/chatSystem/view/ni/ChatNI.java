package chatSystem.view.ni;

import chatSystem.view.gui.View;
import chatSystem.controller.ChatController;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;
import java.util.Observable;
import chatSystemCommon.*;

public class ChatNI extends View implements Runnable {

    private Thread threadReceiver;
    private final MessageReceiver messageReceiver;
    private final MessageTransfert messageTransfert;
    private final FileReceiver[] fileReceiver;
    private final FileTransfert[] fileTransfert;

    public ChatNI(ChatController controller) {
        super(controller);
     
        this.fileReceiver = new FileReceiver[5];
        this.fileTransfert = new FileTransfert[5];
        this.messageReceiver = new MessageReceiver(this);
        this.messageTransfert = new MessageTransfert();
        this.threadReceiver = new Thread(this.messageReceiver);
    }

    public void helloReceived(String username, String ip) {
        ((ChatController) (this.controller)).performHelloReceived(username, ip);
    }

    public void textMessageReceived(Message msg, String ip) {

    }

    public void goodbyeReceived(String username, String ip) {
        ((ChatController) (this.controller)).performGoodbyeReceived(username+ip);
    }

    public void fileTransfertDemandReceived(FileTransfertDemand msg, String ip) {

    }

    public void fileTransfetConfirmationReceived(FileTransfertConfirmation msg, String ip) {

    }

    public void sendHelloMsg(String username, String ip) {
        this.messageTransfert.sendHello(username, ip);
    }

    public void sendHelloMsg(String username) {
        this.messageTransfert.sendHello(username);
    }

    /**
     *
     * @param o : part of the model which send a the notification
     * @param arg : argument sended by the model
     */
    @Override
    public void update(Observable o, Object arg) {          //pas sexy, trouver un moyen de mieux le gérer genre faire des fonction userInfoupdated etc
        System.out.println("Entering update");
        if(o instanceof UserInformation){
            if(arg instanceof UserState){
                if((UserState)arg == UserState.CONNECTED){
                    //on est connecté, on commence l'écoute
                    if(!(this.threadReceiver.getState() == Thread.State.RUNNABLE)){
                        //a changer completement il faudrait pouvoir couper le thread et le lancer comme on veut
                        this.threadReceiver.start();
                    }
                    //et on le guele car on est content :)
                    this.messageTransfert.sendHello(((UserInformation) o).getUsername());
                }
                else{
                    //this.threadReceiver.stop(); //il ne faut pas le stopper donc on lui mettra null mais faut changer la gestion de messageReceiver
                    this.messageTransfert.sendGoodbye(((UserInformation) o).getUsername());
                }
            }
        } 
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
