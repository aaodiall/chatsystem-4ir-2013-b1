/**
 * Responsible of the interface between the local user and the system
 */
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
     * Modify the interface to show the user the progression of his file
     * transfert
     *
     * @param tmp information about the file transfert
     */
    @Override
    public void displayFileTransfertProgression(FileTransfertInformation tmp) {
        String contact = tmp.getIdRemoteSystem();
        if (this.dWindows.containsKey(contact)) {
            if (this.dWindows.get(contact) == null) {
                this.dWindows.remove(contact);
                this.dWindows.put(contact, new DialogWindow(contact,this));
            }
        }
        if (tmp instanceof FileSendingInformation) {
            this.dWindows.get(contact).displayFileSendingProgression(tmp.getId(), tmp.getSize(), tmp.getProgression());
        } else if (tmp instanceof FileReceivingInformation) {
            this.dWindows.get(contact).displayFileReceivingProgression(tmp.getId(), tmp.getSize(), tmp.getProgression());
        }

    }

    /**
     * Modify the interface to show the user a file transfert's suggestion
     *
     * @param tmp
     */
    @Override
    public void displayFileSuggestion(FileReceivingInformation tmp) {
        String contact = tmp.getIdRemoteSystem();
        if (this.dWindows.containsKey(contact)) {
            if (this.dWindows.get(contact) == null) {
                this.dWindows.remove(contact);
                this.dWindows.put(contact, new DialogWindow(contact, this));
            }
        }
        this.dWindows.get(contact).displaySuggestion(tmp.getName(), tmp.getId());
    }

    /**
     * Modify the interface to show the user the dialog window of the remote
     * system he wishes to communicate with
     *
     * @param contact
     */
    @Override
    public void displayDialogWindow(String contact) {
        // La fenetre peut être a null même si on connait le contact
        if (this.dWindows.containsKey(contact)) {
            if (this.dWindows.get(contact) == null) {
                this.dWindows.remove(contact);
                this.dWindows.put(contact, new DialogWindow(contact, this));
            }
            this.dWindows.get(contact).setVisible(true);
        }
    }


    /* @Override
     public void displayNewMessageNotification() {
     }*/
    /**
     * Modify the interface to update the list of available contacts
     *
     * @param newList new list of available contact
     * @throws GUIException security in case the update was launched before
     * creating the user list window
     */
    @Override
    public void listUser(List<String> newList) throws GUIException {

        for (String contact : newList) {
            if (!(this.dWindows.containsKey(contact))) {
                //on mets null pour si on ne lui parle jamais
                this.dWindows.put(contact, null);
            }
        }
        if (this.uWindow == null) {
            throw new GUIException();
        } else {
            this.uWindow.updateContacts(newList);
        }
    }

    /*   @Override
     public void displayMessage() {
     //fait directement avec le dafaultListModel mais il faut le changer pour le faire nous mm (comme la liste utilisateur)
     }*/
    /**
     * Update launched by the modification of some Observable object the class
     * is following
     *
     * @param o : part of the model which send a the notification
     * @param arg : argument sended by the model
     */
    @Override
    public void update(Observable o, Object arg) {          
        System.out.println("Entering update Chat GUI"+o);
        if (o instanceof RemoteSystemInformation) {
            updateByRemoteSystemInformation((RemoteSystemInformation) o, arg);
        } else if (o instanceof UserInformation) {
            if (arg instanceof String) {
                if (uWindow == null) {
                    //premiere connection
                    //on créée la UserWindow avc le pseudo choisi
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
        } else if (o instanceof RemoteSystems) {
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
    
    private void updateByRemoteSystemInformation(RemoteSystemInformation rsi, Object arg) {
        if (arg instanceof String) {
            String id = rsi.getIdRemoteSystem();
            System.out.println("Updating the conversation with the contact "+ id);
            this.displayDialogWindow(id);
            //this.dWindows.get(id).toBack();
            this.dWindows.get(id).updateConversation(arg);
        //this.dWindows.get(id).updateConversation(rsi.getMessages());
        }    
    }
    

    /**
     * Update launched if the modification occured in a FileSendingInformation
     * instance
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
                // Nothing TODO
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
                // Nothing TODO
                break;
        }
    }

    /**
     * Connection of the local user
     *
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
     *
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
     *
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
     *
     * @param idRemoteSystem id of the remote system
     */
    @Override
    public void openDialogWindow(String idRemoteSystem) {
        displayDialogWindow(idRemoteSystem);
    }

    /**
     * Accept the file transfert's suggestion displayed to the local user
     *
     * @param idTransfert id of the file's transfert
     */
    @Override
    public void acceptSuggestion(int idTransfert) {
        ((ChatController) this.controller).performAcceptSuggestion(idTransfert);
    }

    /**
     * Decline the file transfert's suggestion displayed to the local user
     *
     * @param idTransfert id of the file's transfert
     */
    @Override
    public void declineSuggestion(int idTransfert) {
        ((ChatController) this.controller).performDeclineSuggestion(idTransfert);
    }

    /**
     * Send a file transfert's request to a given remote system
     *
     * @param fileToSend file the user wants to send
     * @param idRemoteSystem id of the remote system the file is to be proposed
     * to
     */
    @Override
    public void sendFileRequest(File fileToSend, String idRemoteSystem) {
        ((ChatController) this.controller).performSendFileRequest(fileToSend, idRemoteSystem);
    }
}
