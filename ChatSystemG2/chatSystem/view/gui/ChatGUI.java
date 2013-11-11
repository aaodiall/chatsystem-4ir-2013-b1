package chatSystem.view.gui;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import chatSystem.controller.ChatController;
import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatGUI extends View {

    private ConnectWindow cWindow = null;
    private UserWindow uWindow = null;
    private final Map<String, DialogWindow> dWindows;

    public ChatGUI(ChatController controller) {
        super(controller);
        this.cWindow = new ConnectWindow(this);
        this.dWindows = new HashMap<String, DialogWindow>();
    }

    public void disconnected() {
    }

    public void displayFileTransfertProgression() {

    }

    public void displaySuggestion() {

    }

    public void displayDialogWindow(String contact) {
        if (this.dWindows.containsKey(contact)) {
            if (this.dWindows.get(contact) == null) {
                this.dWindows.remove(contact);
                this.dWindows.put(contact, new DialogWindow(contact));
            } else {
                this.dWindows.get(contact).setVisible(true);
                //this.dWindows.get(contact).setEnabled(true);
            }
        }
    }

    public void displayDeclinedSuggestionNotification() {
    }

    public void displayNewMessageNotification() {
    }

    public void displayReceivedFile() {
    }

    public void displayFileSendedNotification() {
    }

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

    public void displayMessage() {
    }

    public void displayBrowseWindow() {
    }

    public void displayAcceptedSuggestionNotification() {
    }

    public void connectButtonPressed(String username) {
        ((ChatController) this.controller).performConnect(username);
    }
    
    public void deconnectButtonPressed() {
        ((ChatController) this.controller).performDisconnect();
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
                this.uWindow = new UserWindow((String) arg,this);
            } else if (arg instanceof UserState) {
                if ((UserState) arg == UserState.CONNECTED) {
                    cWindow.setVisible(false);
                    uWindow.setVisible(true);
                }else{
                    cWindow.setVisible(true);
                    uWindow.setVisible(false);
                    uWindow = null;
                }
            }
        } else if (o instanceof RemoteSystems) {
            //if (arg instanceof List) {
                try {
                    listUser(((RemoteSystems)o).getUserList());

                } catch (GUIException ex) {
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            //}
        }
    }
}