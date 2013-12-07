package chatSystem.view.gui;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import chatSystem.controller.ChatController;
import chatSystem.model.*;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible of the interface between the local user and the system
 */
public class ChatGUI extends View implements ToUser, FromUser {

    private ConnectWindow cWindow = null;
    private UserWindow uWindow = null;
    private final Map<String, DialogWindow> dWindows;

    /**
     * Class' constructor
     * @param controller instance of controller responsible for this instance of
     * chat gui
     */
    public ChatGUI(ChatController controller) {
        super(controller);
        this.cWindow = new ConnectWindow(this);
        this.dWindows = new HashMap<String, DialogWindow>();
    }

    /**
     * Modify the interface to show the user he is disconnected
     */
    @Override
    public void disconnected() {
        for (String key : this.dWindows.keySet()) {
            DialogWindow aux = this.dWindows.get(key);
            if (aux != null) {
                aux.setVisible(false);
            }
            // this.dWindows.remove(key);
        }
        cWindow.setVisible(true);
        uWindow.setVisible(false);
    }

    /**
     * Modify the interface to show the user he is connected
     */
    @Override
    public void connected() {
        cWindow.setVisible(false);
        uWindow.setVisible(true);
    }

    /**
     * Display an error message to show the user the connection got wrong
     */
    @Override
    public void displayConnectionErrorNotification() {
        this.cWindow.displayConnectionError();
    }
    
    /**
     * Display an error message to show the user there was an error during the file transfert
     * @param idRemoteSystem contact
     * @param fileName name of the file
     */
    @Override
    public void displayFileTransfertErrorNotification(String idRemoteSystem, String fileName) {
        this.getDialogWindow(idRemoteSystem).displayFileTransfertError(fileName);
    }

    /**
     * Modify the interface to show the user the progression of his file
     * transfert
     * @param tmp information about the file transfert
     */
    @Override
    public void displayFileTransfertProgression(FileTransfertInformation tmp) {
        if (tmp instanceof FileSendingInformation) {
            this.getDialogWindow(tmp.getIdRemoteSystem()).displayFileSendingProgression(tmp.getId(), tmp.getSize(), tmp.getProgression());
        } else if (tmp instanceof FileReceivingInformation) {
            this.getDialogWindow(tmp.getIdRemoteSystem()).displayFileReceivingProgression(tmp.getId(), tmp.getSize(), tmp.getProgression());
        }
    }
    
    /**
     * Modify the interface to reset the progression of a file
     * transfert
     * @param tmp information about the file transfert
     */
    @Override
    public void resetFileTransfertProgression(FileTransfertInformation tmp) {
        if (tmp instanceof FileSendingInformation) {
            this.getDialogWindow(tmp.getIdRemoteSystem()).resetFileSendingProgression(tmp.getId(), tmp.getSize());
        } else if (tmp instanceof FileReceivingInformation) {
            this.getDialogWindow(tmp.getIdRemoteSystem()).resetFileReceivingProgression(tmp.getId(), tmp.getSize());
        }
    }

    /**
     * Modify the interface to show the user a file transfert's suggestion
     * @param tmp
     */
    @Override
    public void displayFileSuggestion(FileReceivingInformation tmp) {
        this.getDialogWindow(tmp.getIdRemoteSystem()).displaySuggestion(tmp.getName(), tmp.getId());
    }

    /**
     * Modify the interface to show the user the dialog window of the remote
     * system he wishes to communicate with
     * @param contact
     */
    @Override
    public void displayDialogWindow(String contact) {
        this.getDialogWindow(contact).setVisible(true);
    }

    /**
     * Modify the interface to update the list of available contacts
     * @param newList new list of available contact
     * @throws GUIException security in case the update was launched before
     * creating the user list window
     */
    @Override
    public void listUser(List<String> newList) throws GUIException {
        
        for (String contact: this.dWindows.keySet()) {
            if (!newList.contains(contact))
                this.getDialogWindow(contact).displayUserDisconnected();
        }
        
        for (String contact : newList) {
            if (!(this.dWindows.containsKey(contact))) {
                //contact created with no window associated
                //in case the user never speaks to him
                this.dWindows.put(contact, null);
            }
        }
        if (this.uWindow == null) {
            throw new GUIException();
        } else {
            this.uWindow.updateContacts(newList);
        }
    }
    
    /**
     * Update the Dialog Window of a Remote Sytem by adding a new message
     * @param idRemoteSystem
     * @param newMessage
     */
    @Override
    public void displayMessage(String idRemoteSystem, String newMessage){
        this.dWindows.get(idRemoteSystem).updateConversation(newMessage);
    }
    
    /**
     * Private function which verifies if a Dialog Window for a Remote User is created
     * before returning it
     * @param idRemoteSystem
     * @return the dialogWindow we need
     */
    private DialogWindow getDialogWindow(String idRemoteSystem) {
        if (this.dWindows.containsKey(idRemoteSystem)) {
            if (this.dWindows.get(idRemoteSystem) == null) {
                this.dWindows.remove(idRemoteSystem);
                this.dWindows.put(idRemoteSystem, new DialogWindow(idRemoteSystem, this));
            }
        }
        return this.dWindows.get(idRemoteSystem);
    }

    /**
     * Update launched by the modification of some Observable object the class
     * is following
     * @param o : part of the model which send a the notification
     * @param arg : argument sended by the model
     */
    @Override
    public void update(Observable o, Object arg) {
        //System.out.println("Entering update Chat GUI"+o);
        if (o instanceof RemoteSystemInformation) {
            updateByRemoteSystemInformation((RemoteSystemInformation) o, arg);
        } else if (o instanceof UserInformation) {
            if (arg instanceof String) {
                if (uWindow == null) {
                    //first connection
                    //create the UserWindow with the chosen username
                    this.uWindow = new UserWindow((String) arg, this);
                }
                this.uWindow.setUsername((String) arg);
            } else if (arg instanceof UserState) {
                if ((UserState) arg == UserState.CONNECTED) {
                    connected();
                } else {
                    disconnected();
                }
            }
        } else if (o instanceof RemoteSystems && arg != UserState.MAYBEOFFLINE) {
            //we look that the arg is not the MAYBEOFFLINE State because it leads to a deadlock (see RemoteSystem implementation)
            System.out.println("I am the GUI and I update the connected users");
            try {
                listUser(((RemoteSystems) o).getUserList());

            } catch (GUIException ex) {
                Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (o instanceof FileReceivingInformation) {
            updateByFileReceivingInformation((FileReceivingInformation) o);
        } else if (o instanceof FileSendingInformation) {
            updateByFileSendingInformation((FileSendingInformation) o);
        }
    }

    /**
     * Updates the chatGUI and its components have to do because of the
     * modification of a RemoteSystemInformation instance
     * @param rsi RemoteSystemInformation instance that was modified
     * @param arg modification in the instance
     */
    private void updateByRemoteSystemInformation(RemoteSystemInformation rsi, Object arg) {
        if (arg instanceof String) {
            String id = rsi.getIdRemoteSystem();
            System.out.println("Updating the conversation with the contact " + id);
            this.displayDialogWindow(id);
            this.dWindows.get(id).updateConversation(arg);
        }
    }

    /**
     * Update launched if the modification occured in a FileSendingInformation
     * instance
     *
     * @param tmp instance of FileSendingInformation which was created or
     * modified
     */
    public void updateByFileSendingInformation(FileSendingInformation tmp) {
        switch (tmp.getState()) {
            case ACCEPTED:
                displayFileTransfertProgression(tmp);
                break;
            case WAITANSWER:
                // Nothing TODO
                break;
            case DECLINED:
                // Nothing TODO
                break;
            case TERMINATED:
                // if we're here it's that the controller has detect a problem during the transfert
                // we modify the view in consequence
                resetFileTransfertProgression(tmp);
                break;
        }
    }

    /**
     * Update launched if the modification occured in a FileReceivingInformation
     * instance
     *
     * @param tmp instance of FileReceivingInformation which was created or
     * modified
     */
    public void updateByFileReceivingInformation(FileReceivingInformation tmp) {
        switch (tmp.getState()) {
            case ACCEPTED:
                displayFileTransfertProgression(tmp);
                break;
            case WAITANSWER:
                displayFileSuggestion(tmp);
                break;
            case DECLINED:
                //Nothing TODO
                break;
            case TERMINATED:
                // if we're here it's that the controller has detect a problem during the transfert
                // we modify the view in consequence
                resetFileTransfertProgression(tmp);
                break;
        }
    }

    /**
     * Connection of the local user
     * @param username username chosen for the connection by the local user
     */
    @Override
    public void connect(String username) {
        ((ChatController) this.controller).performConnect(username);
    }

    /**
     * Disconnection of the local user
     */
    @Override
    public void disconnect() {
        ((ChatController) this.controller).performDisconnect();
    }

    /**
     * Send a text message
     * @param message text message's content
     * @param idRemoteSystem id of the remote system the local user wants to
     * send the text message
     */
    @Override
    public void sendMessageRequest(String message, String idRemoteSystem) {
        ((ChatController) this.controller).performSendMessageRequest(message, idRemoteSystem);
    }

    /**
     * Save a file in the computer's memory
     * @param fileToSend file which is to be saved
     * @param idTransfert id of the file's transfert
     */
    @Override
    public void saveFile(File fileToSend, int idTransfert) {
        ((ChatController) this.controller).performSaveFile(fileToSend, idTransfert);
    }

    /**
     * Open the dialog window corresponding to the remote system the user wants
     * to talk to
     * @param idRemoteSystem id of the remote system
     */
    @Override
    public void openDialogWindow(String idRemoteSystem) {
        displayDialogWindow(idRemoteSystem);
    }

    /**
     * Accept the file transfert's suggestion displayed to the local user
     * @param idTransfert id of the file's transfert
     */
    @Override
    public void acceptSuggestion(int idTransfert) {
        ((ChatController) this.controller).performAcceptSuggestion(idTransfert);
    }

    /**
     * Decline the file transfert's suggestion displayed to the local user
     * @param idTransfert id of the file's transfert
     */
    @Override
    public void declineSuggestion(int idTransfert) {
        ((ChatController) this.controller).performDeclineSuggestion(idTransfert);
    }

    /**
     * Send a file transfert's request to a given remote system
     * @param fileToSend file the user wants to send
     * @param idRemoteSystem id of the remote system the file is to be proposed to
     */
    @Override
    public void sendFileRequest(File fileToSend, String idRemoteSystem) {
        ((ChatController) this.controller).performSendFileRequest(fileToSend, idRemoteSystem);
    }
}
