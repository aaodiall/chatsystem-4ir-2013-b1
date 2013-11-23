package chatSystem.view.ni;

import chatSystem.view.gui.View;
import chatSystem.controller.ChatController;
import chatSystem.model.*;
import chatSystem.view.ni.messageTransferts.*;
import java.util.Observable;

public class ChatNI extends View {

    private final Thread threadMessageReceiver;
    private final Thread threadMessageTransfert;
    private final MessageReceiver messageReceiver;
    private final MessageTransferts messageTransfert;
    private final FileReceiver[] fileReceivers;
    private final FileTransfert[] fileTransferts;

    private UserInformation usrInfo;

    private int portClient; // changer la gestion

    public ChatNI(ChatController controller) {
        super(controller);

        this.fileReceivers = new FileReceiver[5];
        this.fileTransferts = new FileTransfert[5];
        this.messageReceiver = new MessageReceiver(this);
        this.messageTransfert = new MessageTransferts(this);
        this.threadMessageReceiver = new Thread(this.messageReceiver);
        this.threadMessageTransfert = new Thread(this.messageTransfert);

        this.portClient = 1024;
    }

    public void helloReceived(String username, String ip) {
        ((ChatController) (this.controller)).performHelloReceived(username, ip);
    }

    public void textMessageReceived(String msg, String username) {
        ((ChatController) (this.controller)).performMessageReceived(msg, username);
    }

    public void goodbyeReceived(String username) {
        ((ChatController) (this.controller)).performGoodbyeReceived(username);
    }

    public void fileTransfertDemandReceived(String name, String username, String ip, long size, int id, int portClient) {
        ((ChatController) (this.controller)).performSuggestionReceived(name, size, RemoteSystemInformation.generateID(username, ip), id);
    }

    public void fileTransfertConfirmationReceived(String ip, int idTransfert, boolean accepted) {
        ((ChatController) (this.controller)).performConfirmationReceived(ip, idTransfert, accepted);
    }

    public void filePartReceived(byte[] filePart, boolean isLast) {
        ((ChatController) (this.controller)).performFilePartReceived(filePart, isLast);
    }

    public void sendFileTransfertConfirmation(boolean isAccepted, int idTransfert, String idRemoteSystem) {
        this.messageTransfert.setFileConfirmationTask(idRemoteSystem, isAccepted, idTransfert);
    }

    /*public void createNewFileTransfert() {
     int i = 0;
     boolean fileTransfertCreated = false;
     while (i < 4 && (!fileTransfertCreated)) {
     if (this.fileTransfert[i] != null && )
     }*/
    /**
     *
     * @param o : part of the model which send a the notification
     * @param arg : argument sended by the model
     */
    @Override
    public void update(Observable o, Object arg) {          //pas sexy, trouver un moyen de mieux le gérer genre faire des fonction userInfoupdated etc
        System.out.println("Entering update ChatNI" + o + " : " + arg);
        if (o instanceof UserInformation) {
            if (arg instanceof UserState) {
                
                updateByUserInformation((UserInformation) o, (UserState) arg);
                
            }
        } else if (o instanceof RemoteSystems) {
            if(arg == null){
                this.messageTransfert.setHelloTask();
            }else if (arg instanceof String) { //ip
                
                this.messageTransfert.setHelloTask((String) arg);        
                
            } else if (arg instanceof RemoteSystemInformation) {
                updateByRemoteSystems((RemoteSystemInformation) arg);
            }
        } else if (o instanceof FileTransferts) {
            if (arg instanceof FileReceivingInformation) {

                updateByFileReceivingInformation((FileReceivingInformation) arg);
                
            } else if (arg instanceof FileSendingInformation) {
                
                updateByFileSendingInformation((FileSendingInformation)arg);

            }

        }
    }
    
    public void updateByFileReceivingInformation(FileReceivingInformation tmp) {
        switch (tmp.getState()) {
            case ACCEPTED:
                System.out.println("Envoi rep demande ok ");
                this.messageTransfert.setFileConfirmationTask(tmp.getIdRemoteSystem(), true, tmp.getId());

                String ip = RemoteSystems.getInstance().getRemoteSystem(tmp.getIdRemoteSystem()).getIP();

                Thread fileReceiver = new Thread(new FileReceiver(ip, portClient, this));
                fileReceiver.setName("Thread Reception TCP");
                fileReceiver.start();
                break;
            case WAITANSWER:
                //NOTHING TODO
                break;
            case DECLINED:

                this.fileTransferts[0] = null;
                break;
            case TERMINATED:
                //Noting TODO
                break;
        }
    }

    public void updateByFileSendingInformation(FileSendingInformation tmp) {
        switch (tmp.getState()) {
            case ACCEPTED:
                //Noting TODO
                break;
            case WAITANSWER:

                this.fileTransferts[0] = new FileTransfert(portClient, tmp.getId(), this);
                Thread fileSender = new Thread(this.fileTransferts[0]);
                fileSender.setName("Thread Envoi TCP");
                fileSender.start();
                this.messageTransfert.setFileDemandTask(tmp.getName(), tmp.getSize(), tmp.getIdRemoteSystem(), this.portClient);

                this.portClient++;

                break;
            case DECLINED:

                this.fileTransferts[0] = null;
                break;
            case TERMINATED:
                //Noting TODO
                break;
        }
    }
    
    public void updateByUserInformation(UserInformation usrInfo, UserState state) {

        if (state == UserState.CONNECTED) {
            //on est connecté, on commence l'écoute
            if (!(this.threadMessageReceiver.getState() == Thread.State.RUNNABLE)) {
                System.out.println("Demarrage de la reception");
                //a changer completement il faudrait pouvoir couper le thread et le lancer comme on veut
                this.usrInfo = usrInfo;
                this.threadMessageReceiver.start();
                this.threadMessageReceiver.setName("Thread Reception UDP");
                this.threadMessageTransfert.start();
                this.threadMessageTransfert.setName("Thread Envoi UDP");
            }
            //et on le guele car on est content :)
            this.messageTransfert.setHelloTask();
        } else {
            this.messageTransfert.setGoodbyeTask();
            //this.threadReceiver = null;
        }
    }

    public void updateByRemoteSystems(RemoteSystemInformation aux) {
        String msgToSend = aux.getMessageToSend();
        if (msgToSend != null) {
            this.messageTransfert.setTextMessageTask(aux.getIdRemoteSystem(), msgToSend);
        }
    }

    public void messageSent(String msg, String idRemoteSystem) {
        ((ChatController) this.controller).performMessageSent(msg, idRemoteSystem);
    }

    public UserInformation getUserInfo() {
        return this.usrInfo;
    }

}
