package chatSystem.view.ni;

import chatSystem.view.gui.View;
import chatSystem.controller.ChatController;
import chatSystem.model.*;
import chatSystem.view.ni.messageTransferts.*;
import java.net.SocketException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * View of the network controls the sending and receiving of messages and files
 */
public class ChatNI extends View {

    private Thread threadMessageReceiver;
    private Thread threadMessageTransfert;
    private MessageReceiver messageReceiver;
    private final MessageTransferts messageTransfert;

    private UserInformation usrInfo;

    /**
     * Class' constructor
     *
     * @param controller instance of ChatController that controls this view
     */
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

    /**
     * Informs the controller of the reception of a hello message
     *
     * @param username username of the contact who sent the hello message
     * @param ip ip adress of the contact
     * @param isAck whether or not the contact expects an answer
     */
    public void helloReceived(String username, String ip, boolean isAck) {
        ((ChatController) (this.controller)).performHelloReceived(username, ip, isAck);
    }

    /**
     * Informs the controller of the reception of a text message
     *
     * @param msg text message's content
     * @param username username of the contact who sent the text message
     */
    public void textMessageReceived(String msg, String username) {
        ((ChatController) (this.controller)).performMessageReceived(msg, username);
    }

    /**
     * Informs the controller of the reception of a goodbye message
     *
     * @param username username of the contact who sent the goodbye message
     */
    public void goodbyeReceived(String username) {
        ((ChatController) (this.controller)).performGoodbyeReceived(username);
    }

    /**
     * Informs the controller of the reception of a file transfert request
     *
     * @param name file's name
     * @param username username of the contact who sent the request
     * @param ip ip adress of the contact
     * @param size file's size
     * @param id transfert's id
     * @param portServer port the transfert is going to use
     */
    public void fileTransfertDemandReceived(String name, String username, String ip, long size, int id, int portServer) {
        ((ChatController) (this.controller)).performSuggestionReceived(name, size, RemoteSystemInformation.generateID(username, ip), id, portServer);
    }

    /**
     * Informs the controller of the reception of a file transfert confirmation
     *
     * @param ip ip adress of the contact who answered the request
     * @param idTransfert transfert's id
     * @param accepted whether or not the contact accepted the transfert
     */
    public void fileTransfertConfirmationReceived(String ip, int idTransfert, boolean accepted) {
        ((ChatController) (this.controller)).performConfirmationReceived(ip, idTransfert, accepted);
    }

    /**
     * Informs the controller of the reception of a file's part
     *
     * @param idTransfert transfert's id
     * @param filePart file part received
     * @param isLast whether or not this is the last part of the transfert
     */
    public void filePartReceived(int idTransfert, byte[] filePart, boolean isLast) {
        ((ChatController) (this.controller)).performFilePartReceived(idTransfert, filePart, isLast);
    }

    /**
     * Informs the controller of the sending of a file
     *
     * @param idTransfert transfert's id
     * @param idRemoteSystem id of the remote system the file was sent to
     */
    public void fileSended(int idTransfert, String idRemoteSystem) {
        ((ChatController) (this.controller)).performFileSended(idTransfert, idRemoteSystem);
    }

    /**
     * Update launched when one of the Observable objects the chatNI is
     * following is modified
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
            if (arg == null || arg == UserState.MAYBEOFFLINE) {
                //the arg can be null or the MAYBEOFFLINE State because we need this trick to avoid a deadlock (see RemoteSystem implementation)
                this.messageTransfert.setHelloTask();
            } else if (arg instanceof String) { //ip
                this.messageTransfert.setHelloTask((String) arg);
            } else if (arg instanceof RemoteSystemInformation) {
                updateByRemoteSystems((RemoteSystemInformation) arg);
            }
        } else if (o instanceof FileReceivingInformation && arg == null) {
            //testing arg == null avoids to analyze the progression notifications
            updateByFileReceivingInformation((FileReceivingInformation) o);
        } else if (o instanceof FileSendingInformation && arg == null) {
            updateByFileSendingInformation((FileSendingInformation) o);
        }
    }

    /**
     * Update launched when an instance of FileReceivingInformation is modified
     *
     * @param tmp instance of FileReceivingInformation that was modified
     */
    private void updateByFileReceivingInformation(FileReceivingInformation tmp) {
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

    /**
     * Update launched when an instance of FileSendingInformation is modified
     *
     * @param tmp instance of FileSendingInformation that was modified
     */
    private void updateByFileSendingInformation(FileSendingInformation tmp) {
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

    /**
     * Update launched when the instance of UserInformation is modified
     *
     * @param usrInfo instance of UserInformation that was modified
     * @param state new user's state
     */
    private void updateByUserInformation(UserInformation usrInfo, UserState state) {

        if (state == UserState.CONNECTED) {
            //create and start new Threads
            //this.gestionThread();
            //system connected, starting to wait for messages
            if (!(this.threadMessageReceiver.getState() == Thread.State.RUNNABLE)) {
                this.gestionThread();
                System.out.println("Demarrage de la reception");
                this.usrInfo = usrInfo;
                this.threadMessageReceiver.start();
                this.threadMessageReceiver.setName("Thread Reception UDP");
                this.threadMessageTransfert.start();
                this.threadMessageTransfert.setName("Thread Envoi UDP");
            }
            this.messageTransfert.setHelloTask();
        } else {
            this.messageTransfert.setGoodbyeTask();
        }
    }

    /**
     * Private function to create new threads
     */
    private synchronized void gestionThread() {
        if (!this.threadMessageReceiver.isAlive()) {
            this.threadMessageReceiver = new Thread(this.messageReceiver);
        }
        if (!this.threadMessageTransfert.isAlive()) {
            this.threadMessageTransfert = new Thread(this.messageTransfert);
        }
    }

    /**
     * Update launched when the instance of RemoteSystems alerts a instance of
     * RemoteSystemInformation was modified We decided to receive the
     * notification from RemoteSystems to centralize the information and for the
     * chatNI not to be disturbed by the RemoteSystemInformation instances'
     * notifications for the chatGUI
     *
     * @param aux instance of RemoteSystemInformation that was modified
     */
    private void updateByRemoteSystems(RemoteSystemInformation aux) {
        String msgToSend = aux.getMessageToSend();
        if (msgToSend != null) {
            this.messageTransfert.setTextMessageTask(aux.getIdRemoteSystem(), msgToSend);
        }
    }

    /**
     * Informs the controller a text message was sent
     *
     * @param msg text message's content
     * @param idRemoteSystem id of the remote system the message has been sent
     * to
     */
    public void messageSent(String msg, String idRemoteSystem) {
        ((ChatController) this.controller).performMessageSent(msg, idRemoteSystem);
    }

    /**
     * Return the UserInformation instance
     *
     * @return information about the local user
     */
    public UserInformation getUserInfo() {
        return this.usrInfo;
    }

    /**
     * Informs the controller there has been an error during a file transfert
     *
     * @param idTransfert transfert's id
     */
    public void fileTransfertError(int idTransfert) {
        ((ChatController) controller).performFileTransfertError(idTransfert);
    }
}
