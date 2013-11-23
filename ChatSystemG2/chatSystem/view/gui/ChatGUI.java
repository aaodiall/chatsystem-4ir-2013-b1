package chatSystem.view.gui;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import chatSystem.controller.ChatController;
import chatSystem.model.FileReceivingInformation;
import chatSystem.model.FileTransferts;
import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatGUI extends View implements ToUser, FromUser{

    private ConnectWindow cWindow = null;
    private UserWindow uWindow = null;
    private final Map<String, DialogWindow> dWindows;

    public ChatGUI(ChatController controller) {
        super(controller);
        this.cWindow = new ConnectWindow(this);
        this.dWindows = new HashMap<String, DialogWindow>();
    }

    @Override
    public void disconnected() {
        cWindow.setVisible(true);
        uWindow.setVisible(false);
    }
    
    @Override
    public void connected(){
        cWindow.setVisible(false);
        uWindow.setVisible(true);
    }

    @Override
    public void displayFileTransfertProgression() {

    }

    @Override
    public void displaySuggestion(FileReceivingInformation tmp) {

        this.dWindows.get(tmp.getIdRemoteSystem()).displaySuggestion(tmp.getName(), tmp.getId());
    }

    @Override
    public void displayDialogWindow(String contact) {
        if (this.dWindows.containsKey(contact)) {
            if (this.dWindows.get(contact) == null) {
                this.dWindows.remove(contact);
                this.dWindows.put(contact, new DialogWindow(contact, RemoteSystems.getInstance().getRemoteSystem(contact).getMessages(),this));
            }
            this.dWindows.get(contact).setVisible(true);

        }
    }

    @Override
    public void displayDeclinedSuggestionNotification() {
    }

    @Override
    public void displayNewMessageNotification() {
    }

    @Override
    public void displayReceivedFile() {
    }

    @Override
    public void displayFileSendedNotification() {
    }

    @Override
    public void listUser(List<String> newList) throws GUIException {

        for (String contact : newList) {
            if (!(this.dWindows.containsKey(contact))) {
                this.dWindows.put(contact, null);
            }
        }
        if (this.uWindow == null) {
            throw new GUIException();
        } else {
            this.uWindow.updateContacts(newList);
        }
    }

    @Override
    public void displayMessage() {
        //fait directement avec le dafaultListModel mais il faut le changer pour le faire nous mm (comme la liste utilisateur)
    }

    @Override
    public void displayAcceptedSuggestionNotification() {
    }
    
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
                displaySuggestion((FileReceivingInformation)arg);
            }
            
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
    public void selectDirectory(String directory) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void openDialogWindow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void confirmSending() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendFileRequest(File fileToSend, String idRemoteSystem) {
        ((ChatController) this.controller).performSendFileRequest(fileToSend, idRemoteSystem);
    }

    @Override
    public void selectFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
}
