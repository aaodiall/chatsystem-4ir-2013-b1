package chatSystem.view.gui;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import chatSystem.controller.ChatController;
import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;
import java.util.ArrayList;

public class ChatGUI extends View {

    private ConnectWindow cWindow = null;
    private UserWindow uWindow = null;
    private Map<String, DialogWindow> dWindows;

    public ChatGUI(ChatController controller) {
        super(controller);
        this.cWindow = new ConnectWindow(this);
    }

    public void Disconnected() {
    }

    public void DisplayFileTransfertProgression() {

    }

    public void DisplaySuggestion() {

    }

    public void DisplayDialogWindow(String contact) {
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

    public void DisplayDeclinedSuggestionNotification() {
    }

    public void DisplayNewMessageNotification() {
    }

    public void DisplayReceivedFile() {
    }

    public void DisplayFileSendedNotification() {
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

    public void DisplayMessage() {
    }

    public void DisplayBrowseWindow() {
    }

    public void DisplayAcceptedSuggestionNotification() {
    }
    
    public void connectButtonPressed(String username){
        ((ChatController)this.controller).performConnect(username);
    }

    /**
     *
     * @param o : part of the model which send a the notification
     * @param arg : argument sended by the model
     */
    @Override
    public void update(Observable o, Object arg) {          //pas sexy, trouver un moyen de mieux le gérer
        System.out.println("Entering update");
        if(o instanceof UserInformation){
            if(arg instanceof String){
                this.uWindow = new UserWindow((String) arg);
            }else if(arg instanceof UserState){
                if((UserState)arg == UserState.CONNECTED){
                    cWindow.setVisible(false);
                    uWindow.setVisible(true);
                }
            }
        } 
        else if(o instanceof RemoteSystems){
            if(arg instanceof List){
                this.uWindow.updateContacts((List<String>)arg);
            }
        }
    }
}
