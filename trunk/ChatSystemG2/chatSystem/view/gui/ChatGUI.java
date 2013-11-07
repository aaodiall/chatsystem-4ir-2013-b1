package chatSystem.view.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class ChatGUI extends View implements ActionListener{
	
	private ConnectWindow cWindow = null;
	private UserWindow uWindow = null;
	private Map<String,DialogWindow> dWindows;

	public ChatGUI()
	{
		this.cWindow = new ConnectWindow(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void Disconnected() {
	}

	public void DisplayFileTransfertProgression() {
		
	}
	
	public void DisplaySuggestion() {
		
	}
	
	public void DisplayDialogWindow (String contact) {
		if (this.dWindows.containsKey(contact)) {
			if (this.dWindows.get(contact) == null) {
				this.dWindows.remove(contact);
				this.dWindows.put(contact, new DialogWindow(contact));
			}
			else {
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
	
	public void listUser(List<String> newList) throws GUIException{
		for(String contact: newList) {
			if (!(this.dWindows.containsKey(contact))) {
				this.dWindows.put(contact, null);
			}
		}
		if (this.uWindow == null) {
			throw new GUIException();
		}
		else {
			this.uWindow.updateContacts(newList);
		}
	}
	
	public void DisplayMessage() {
	}
	
	public void DisplayBrowseWindow() {
	}
	
	public void DisplayAcceptedSuggestionNotification() {
	}

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
