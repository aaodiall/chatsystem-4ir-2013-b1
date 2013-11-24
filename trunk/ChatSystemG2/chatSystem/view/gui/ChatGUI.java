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

public class ChatGUI extends View implements ToUser, FromUser{

    private ConnectWindow cWindow = null;
    private UserWindow uWindow = null;
    private final Map<String, DialogWindow> dWindows;

    /**
     * Class' constructor
     * @param controller instance of controller responsible for this instance of chat gui
     */
    public ChatGUI(ChatController controller) {
        super(controller);
        this.cWindow = new ConnectWindow(this);
        this.dWindows = new HashMap<String, DialogWindow>();
    }

    /**
     * 
     */
    @Override
    public void disconnected() {
        cWindow.setVisible(true);
        uWindow.setVisible(false);
        for (String key: this.dWindows.keySet()) {
            this.dWindows.get(key).setVisible(false);
        }
    }
    
    @Override
    public void connected(){
        cWindow.setVisible(false);
        uWindow.setVisible(true);
    }

    @Override
    public void displayFileTransfertProgression(FileTransfertInformation tmp) {
        String contact = tmp.getIdRemoteSystem();
        if (this.dWindows.containsKey(contact)) {
            if (this.dWindows.get(contact) == null) {
                this.dWindows.remove(contact);
                this.dWindows.put(contact, new DialogWindow(contact, RemoteSystems.getInstance().getRemoteSystem(contact).getMessages(), this));
            }
        }
        this.dWindows.get(contact).displayFileSendingProgression(tmp.getId(), tmp.getSize(), tmp.getProgression());
    }

    @Override
    public void displayFileSuggestion(FileReceivingInformation tmp) {
        String contact = tmp.getIdRemoteSystem();
        if (this.dWindows.containsKey(contact)) {
            if (this.dWindows.get(contact) == null) {
                this.dWindows.remove(contact);
                this.dWindows.put(contact, new DialogWindow(contact, RemoteSystems.getInstance().getRemoteSystem(contact).getMessages(), this));
            }
        }
        this.dWindows.get(contact).displaySuggestion(tmp.getName(), tmp.getId());
    }
    

    @Override
    public void displayDialogWindow(String contact) {
        // La fenetre peut être a null même si on connait le contact
        if (this.dWindows.containsKey(contact)) {
            if (this.dWindows.get(contact) == null) {
                this.dWindows.remove(contact);
                this.dWindows.put(contact, new DialogWindow(contact, RemoteSystems.getInstance().getRemoteSystem(contact).getMessages(),this));
            }
            this.dWindows.get(contact).setVisible(true);
        }
    }


   /* @Override
    public void displayNewMessageNotification() {
    }*/


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
     *
     * @param o : part of the model which send a the notification
     * @param arg : argument sended by the model
     */
    @Override
    public void update(Observable o, Object arg) {          //pas sexy, trouver un moyen de mieux le gérer
        System.out.println("Entering update Chat GUI");
        if (o instanceof UserInformation) {
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
            
            System.out.println("Je suis la GUI je mets a jour les utilisateur co");
            try {
                listUser(((RemoteSystems) o).getUserList());

            } catch (GUIException ex) {
                Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else if (o instanceof FileTransferts) {
            
            if (arg instanceof FileReceivingInformation){
                updateByFileReceivingInformation((FileReceivingInformation) arg);
            }else if(arg instanceof FileSendingInformation){
                updateByFileSendingInformation((FileSendingInformation) arg);
            }
            
        }
    }
    
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
    
    @Override
    public void connect(String username) {
        ((ChatController) this.controller).performConnect(username);
    }

    @Override
    public void disconnect() {
        ((ChatController) this.controller).performDisconnect();
    }

    @Override
    public void sendMessageRequest(String message, String idRemoteSystem) {
        ((ChatController) this.controller).performSendMessageRequest(message,idRemoteSystem);
    }

    @Override
    public void saveFile(File fileToSend, int idTransfert) {
        ((ChatController) this.controller).performSaveFile(fileToSend, idTransfert);
    }

    @Override
    public void openDialogWindow(String idRemoteSystem) {
        displayDialogWindow(idRemoteSystem);
    }

    @Override
    public void acceptSuggestion(int idTransfert) {
        ((ChatController) this.controller).performAcceptSuggestion(idTransfert);
    }
    
    @Override
    public void declineSuggestion(int idTransfert) {
        ((ChatController) this.controller).performDeclineSuggestion(idTransfert);
    }

    @Override
    public void sendFileRequest(File fileToSend, String idRemoteSystem) {
        ((ChatController) this.controller).performSendFileRequest(fileToSend, idRemoteSystem);
    }
}
