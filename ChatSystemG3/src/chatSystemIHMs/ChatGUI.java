/**
 * 
 */
package chatSystemIHMs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import chatSystemController.Controller;
import chatSystemModel.ModelFileToSend;
import chatSystemModel.ModelListUsers;
import chatSystemModel.ModelStates;
import chatSystemModel.ModelText;
import chatSystemModel.ModelUsername;
import chatSystemModel.ModelFileToReceive;

/**
 * This class creates and manipulates all the IHMs
 * 
 * @author Alpha DIALLO & Joanna VIGNE
 * @version 1.0
 */


public class ChatGUI implements Observer,ToUser,FromUser{
	private InterfaceConnect wConnect;
	private Map<String,InterfaceCommunicate > wCommunicate;
	private InterfaceListUsers wListUsers=null;;
	@SuppressWarnings("unused")
	private InterfaceDialogFile wDialogFile;
	private Controller controller;

/**
 *  
 * The constructor of ChatGUI
 * @param controller
 * 
 * 
 */
	
	public ChatGUI(Controller controller) {
		this.controller=controller;
		this.initConnection();
	}

/**
 * This method is called whenever the ModelState object is changed.
 * update automatically the view when the user disconnect
 * @param arg1
 */
	public void updateModelStates(Object arg1){
		if (arg1.equals(false)){
			this.wListUsers.getUsers().clear();
			Iterator<InterfaceCommunicate> it = this.wCommunicate.values().iterator();
			while (it.hasNext()){
				it.next().dispose();
			}
			this.wCommunicate.clear();
			this.wListUsers.setVisible(false);
			wConnect.setVisible(true);
			wConnect.setTfdUsername("");
		}
	}

/**
 * This method is called whenever the ModelText object is changed.
 * update automatically the view when the user receive a message
 * @param arg0
 * @param arg1
 */
	public void updateModelText(Observable arg0,Object arg1){
		String txtReceived=new String(((ModelText)arg0).getTextReceived().trim());
		String remote=new String(((ModelText)arg0).getRemote());
		this.displayMessage(txtReceived,remote);
	}

/**
 * This method is called whenever the ModelListUsers object is changed.
 * update automatically the view when the remote user connect or disconnect
 * @param arg1
 */
	public void updateModelListUsers(Object arg1){
		this.wListUsers.setUsers(((HashMap<?, ?>)arg1 ).keySet().toArray()) ;
	}
	
	
	public void udpateFilePropositionReceived(ModelFileToReceive arg1){
		this.proposeFile(arg1.getRemote(),arg1.getName(),arg1.getSize());
	}
	
	/**
	 * This method is called whenever the ModelFileToSend object is changed.
	 * update automatically the view when the user receive a File
	 * @param arg1
	 */
	public synchronized void updateModelFileProgress(Object arg1){
		String remote;
		int progress;
		if (arg1 instanceof ModelFileToSend){
			remote=new String(((ModelFileToSend)arg1).getRemote());
			progress=((ModelFileToSend)arg1).getProgression();
		}else{
			remote=new String(((ModelFileToReceive)arg1).getRemote());
			progress=((ModelFileToReceive)arg1).getProgression();
		}
		this.wCommunicate.get(remote).getProgressBarFile().setValue(progress);
		if(progress==100){
			if (arg1 instanceof ModelFileToSend){
				this.wCommunicate.get(remote).getProgressBarFile().setVisible(false);
				this.wCommunicate.get(remote).getProgressBarFile().setValue(0);
			}else if(arg1 instanceof ModelFileToReceive){
				this.wCommunicate.get(remote).getProgressBarFile().setVisible(false);
				this.wCommunicate.get(remote).getProgressBarFile().setValue(0);
				this.wCommunicate.get(((ModelFileToReceive)arg1).getRemote()).getBtnFileReceived().setVisible(true);
			}
		}	
	}

	public void updateModelFileRefused(Object arg1){
		// on rend la scroll bar invisible
		this.wCommunicate.get(((ModelFileToSend)arg1).getRemote()).getProgressBarFile().setVisible(false);
		this.wCommunicate.get(((ModelFileToSend)arg1).getRemote()).getProgressBarFile().setValue(0);
		System.out.println("gui know file refused");
		/*
		 * PARTIE MESSAGE A L'UTILISATEUR A IMPLEMENTER
		 */
	}
	
	/**
	 * This method is called whenever the ModelUsername object is changed.
	 * update automatically the view when the user connected
	 * @param arg1
	 */
	public void updateModelUsername(Object arg1){
		//premiere connection on cree la liste users
		if(this.wListUsers==null){
			this.wListUsers=new InterfaceListUsers(((String)arg1), this);
		}else {
			this.wListUsers.setLblUsername((String)arg1);
		}
		wConnect.setVisible(false);
		this.wListUsers.setVisible(true);
	}

	/**
	 * This method is called whenever the observed object is changed. 
	 * An application calls an Observable object's notifyObservers method to have all the object's observers notified of the change. 
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof ModelStates){
			this.updateModelStates(arg1);
		}else if(arg0 instanceof ModelText){
			this.updateModelText(arg0,arg1);
		}else if(arg0 instanceof ModelListUsers){
			this.updateModelListUsers(arg1);
		}else if(arg0 instanceof ModelUsername){
			this.updateModelUsername(arg1);
		}else if(arg0 instanceof ModelFileToSend){
			if (((ModelFileToSend)arg0).isRefused() == true){
				this.updateModelFileRefused(arg1);
			}else{
				this.updateModelFileProgress(arg1);
			}
		}else if(arg0 instanceof ModelFileToReceive){
			if (((ModelFileToReceive)arg1).getStateReceivedDemand()){
				this.udpateFilePropositionReceived((ModelFileToReceive)arg1);
			}else if (arg1 instanceof ModelFileToReceive){
				this.updateModelFileProgress(arg1);
			}
		}
	}
	

	/**
	 * is called when the user pushes connect button or presses enter
	 */
	@Override
	public void connect() {
		this.controller.performConnect(this.wConnect.getTfdUsername().trim());
	}


	/**
	 * is called when the user pushes disconnect button
	 */
	@Override
	public void disconnect() {
		controller.performDisconnect();
	}

	/**
	 * is called when the user pushes send message button
	 * @param remoteUsername 
	 */
	@Override
	public void sendMessage(String remoteUsername) {
		String localUsername=this.wListUsers.getLblUsername();
		String text2Send =this.wCommunicate.get(remoteUsername).gettAreaMessageText().trim();
		this.wCommunicate.get(remoteUsername).settAreaHistoryCom(localUsername+" :"+text2Send+"\n");
		controller.performSendText(remoteUsername,wCommunicate.get(remoteUsername).gettAreaMessageText().trim());
		this.wCommunicate.get(remoteUsername).settAreaMessageText("");
	}
	
	/**
	 * is called when the user pushes Join File button
	 * @param remote
	 * 			a remote username
	 */
	@Override
	public void sendFile(String remote) {
		this.openInterfaceDialogFile();
		if (InterfaceDialogFile.getDialogue().showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			String filePath=InterfaceDialogFile.getDialogue().getSelectedFile().getPath();
			this.wCommunicate.get(remote).getProgressBarFile().setVisible(true);
			try {
				this.controller.performPropositionFile(remote, filePath);
			} catch (TransferException e) {
				if(e.isSizeTransfer())
					e.handleSizeTransfer();
				else
					e.handleTooManyFiles();
			}
		}
	}

	/**
	 * is called when the user responds to a receive file demand
	 * @param file
	 * 			name of the file
	 * @param answer
	 * 			answer of the user
	 */
	@Override
	public void receiveFile(String remote,String file, int answer) {	
		if (answer == JOptionPane.YES_OPTION) {
			this.wCommunicate.get(remote).getProgressBarFile().setVisible(true);
			try {
				this.controller.performFileAnswer(remote,file, true);
			} catch (TransferException e) {
				e.handleSizeTransfer();
			}        	
		}
		else if (answer == JOptionPane.NO_OPTION){
			try {
				this.controller.performFileAnswer(remote,file, false);
			} catch (TransferException e) {
				e.handleSizeTransfer();
			}
		}
	}


/**
 * open a conversion window
 * @param remoteUsername
 */
	@Override
	public void openWindowCommunicate(String remoteUsername) {
		if (this.wCommunicate.containsKey(remoteUsername)) {
			if (this.wCommunicate.get(remoteUsername) == null) {
				this.wCommunicate.remove(remoteUsername);
				this.wCommunicate.put(remoteUsername, new InterfaceCommunicate(remoteUsername, this));
			}
			this.wCommunicate.get(remoteUsername).setVisible(true);
		}
		else{
			this.wCommunicate.put(remoteUsername, new InterfaceCommunicate(remoteUsername, this));
			this.wCommunicate.get(remoteUsername).setVisible(true);
		}
	}
/**
 * displays the message received by the user
 * @param text
 * 			message sent by the remote user
 * @param remote
 * 			a remote username
 */
	@Override
	public void displayMessage(String text, String remote) {
		this.openWindowCommunicate(remote);
		this.wCommunicate.get(remote).settAreaHistoryCom("   "+remote+" :"+text+"\n");	
	}


/**
 * is called when the user receives a proposition File from the remote user 
 * @param remote
 * 			a remote username
 * @param file
 * 			file name
 * @param size
 * 			file size
 */
	@Override
	public void proposeFile(String remote, String file, long size) {
		String title=new String("Download File Proposition");
		String message=new String(remote+" vous envoi ce fichier "+file+" de taille "+size);
		this.openWindowCommunicate(remote);
		int answer = JOptionPane.showConfirmDialog(this.wCommunicate.get(remote), message, title,JOptionPane.YES_NO_OPTION);
		this.receiveFile(remote,file, answer);
	}

/**
 * opens window which allows the user to select file in his local disk 
 */
	@Override
	public void openInterfaceDialogFile() {
		this.wDialogFile=new InterfaceDialogFile();
	}

/**
 * initializes the connection window
 */
	@Override
	public void initConnection() {
		this.wCommunicate=new HashMap<String,InterfaceCommunicate>();
		this.wConnect = new InterfaceConnect(this);
		this.wListUsers=new InterfaceListUsers("",this);
	}


	/* notifies the user when he receive a file
	 * @param remote
	 * 			a remote username
	 * (non-Javadoc)
	 * @see chatSystemIHMs.ToUser#notifyFileReceived(java.lang.String)
	 */
	@Override
	public void notifyFileReceived(String remote) {
		JOptionPane.showMessageDialog(this.wCommunicate.get(remote), "File Receveid and Stocked in directory Download",
				"File",
				JOptionPane.WARNING_MESSAGE);
		this.wCommunicate.get(remote).getBtnFileReceived().setVisible(false);
	}

}