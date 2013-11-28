package chatSystem.view.ni;

import chatSystem.view.gui.View;
import chatSystem.controller.ChatController;
import chatSystem.model.*;
import chatSystem.view.ni.messageTransferts.*;
import java.net.SocketException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatNI extends View {

    private Thread threadMessageReceiver;
    private Thread threadMessageTransfert;
    private MessageReceiver messageReceiver;
    private final MessageTransferts messageTransfert;

    private UserInformation usrInfo;

    public ChatNI(ChatController controller) {
        super(controller);
        try {
            this.messageReceiver = new MessageReceiver(this);
        } catch (SocketException ex) {
            ((ChatController) controller).performConnectionError();
            Logger.getLogger(ChatNI.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.messageTransfert = new MessageTransferts(this);
        this.threadMessageReceiver = new Thread(this.messageReceiver);
        this.threadMessageTransfert = new Thread(this.messageTransfert);
    }

    public void helloReceived(String username, String ip, boolean isAck) {
        ((ChatController) (this.controller)).performHelloReceived(username, ip, isAck);
    }

    public void textMessageReceived(String msg, String username) {
        ((ChatController) (this.controller)).performMessageReceived(msg, username);
    }

    public void goodbyeReceived(String username) {
        ((ChatController) (this.controller)).performGoodbyeReceived(username);
    }

    public void fileTransfertDemandReceived(String name, String username, String ip, long size, int id, int portServer) {
        ((ChatController) (this.controller)).performSuggestionReceived(name, size, RemoteSystemInformation.generateID(username, ip), id, portServer);
    }

    public void fileTransfertConfirmationReceived(String ip, int idTransfert, boolean accepted) {
        ((ChatController) (this.controller)).performConfirmationReceived(ip, idTransfert, accepted);
    }

    public void filePartReceived(int idTransfert, byte[] filePart, boolean isLast) {
        ((ChatController) (this.controller)).performFilePartReceived(idTransfert, filePart, isLast);
    }

    public void fileSended(int idTransfert, String idRemoteSystem) {
        ((ChatController) (this.controller)).performFileSended(idTransfert, idRemoteSystem);
    }

    /**
     *
     * @param o : part of the model which send a the notification
     * @param arg : argument sended by the model
     */
    @Override
    public void update(Observable o, Object arg) {          
        //System.out.println("Entering update ChatNI" + o + " : " + arg);
        if (o instanceof UserInformation) {
            if (arg instanceof UserState) {

                updateByUserInformation((UserInformation) o, (UserState) arg);

            }
        } else if (o instanceof RemoteSystems) {
            if (arg == null) {
                this.messageTransfert.setHelloTask();
            } else if (arg instanceof String) { //ip

                this.messageTransfert.setHelloTask((String) arg);

            } else if (arg instanceof RemoteSystemInformation) {
                updateByRemoteSystems((RemoteSystemInformation) arg);
            }
        } else if (o instanceof FileReceivingInformation && arg == null) { // arg == null -> evite de recevoir les notification de progression

            updateByFileReceivingInformation((FileReceivingInformation) o);

        } else if (o instanceof FileSendingInformation && arg == null) {

            updateByFileSendingInformation((FileSendingInformation) o);

        }

    }

    public void updateByFileReceivingInformation(FileReceivingInformation tmp) {
        switch (tmp.getState()) {
            case ACCEPTED:
                this.messageTransfert.setFileConfirmationTask(tmp.getIdRemoteSystem(), true, tmp.getId());

                String ip = RemoteSystems.getInstance().getRemoteSystem(tmp.getIdRemoteSystem()).getIP();

                Thread fileReceiver = new Thread(new FileReceiver(tmp, ip, tmp.getPortServer(), this));
                fileReceiver.setName("Thread Reception TCP");
                fileReceiver.start();
                break;
            case WAITANSWER:
                //NOTHING TODO
                break;
            case DECLINED:
                this.messageTransfert.setFileConfirmationTask(tmp.getIdRemoteSystem(), false, tmp.getId());
                //this.fileTransferts[0] = null;
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

                //this.fileTransferts[0] = new FileTransfert(tmp.getId(), this);
                FileTransfert test = new FileTransfert(tmp.getId(), this);
                Thread fileSender = new Thread(test);
                fileSender.setName("Thread Envoi TCP");
                fileSender.start();
                this.messageTransfert.setFileDemandTask(tmp.getName(), tmp.getSize(), tmp.getIdRemoteSystem(), test.getPort(), tmp.getId());

                break;
            case DECLINED:
                //détruire ?
                //this.fileTransferts[0] = null;
                break;
            case TERMINATED:
                //Noting TODO
                break;
        }
    }

    public void updateByUserInformation(UserInformation usrInfo, UserState state) {

        if (state == UserState.CONNECTED) {
            //create and start new Threads
            // this.gestionThread();
            //on est connecté, on commence l'écoute
            if (!(this.threadMessageReceiver.getState() == Thread.State.RUNNABLE)) {
                this.gestionThread();
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

    private synchronized void gestionThread() {
        if (!this.threadMessageReceiver.isAlive()) {
            this.threadMessageReceiver = new Thread(this.messageReceiver);
            // this.threadMessageReceiver.start();
        }
        if (!this.threadMessageTransfert.isAlive()) {
            this.threadMessageTransfert = new Thread(this.messageTransfert);
            //   this.threadMessageTransfert.start();
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

    public void fileTransfertError(int idTransfert) {
        ((ChatController)controller).performFileTransfertError(idTransfert);
    }
}
