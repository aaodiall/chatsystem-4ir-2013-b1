/**
 * 
 */
package chatSystemIHMs;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import chatSystemController.Controller;
import chatSystemModel.ModelFile;
import chatSystemModel.ModelFileToReceive;
import chatSystemModel.ModelFileToSend;
import chatSystemModel.ModelListUsers;
import chatSystemModel.ModelStates;
import chatSystemModel.ModelText;
import chatSystemModel.ModelUsername;

/**
 * This class creates and manipulates all the IHMs
 * 
 * @author Alpha DIALLO & Joanna VIGNE
 * @version 1.0
 */


public class ChatGUI extends View implements Observer,ToUser,FromUser{
	private InterfaceConnect wConnect;
	private Map<String,InterfaceCommunicate > wCommunicate;
	private InterfaceListUsers wListUsers=null;;
	private CommandLine cmd;
	private InterfaceDialogFile wDialogFile;
	private Controller controller;
	private int mode; 




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
 * @see chatSystemController.ModelStates
 * @param arg1
 */
	public void updateModelStates(Object arg1){
		if ((Boolean)arg1.equals(false)){
			if (this.mode != 0){
				this.wListUsers.getUsers().clear();
				this.wCommunicate.clear();
				this.wListUsers.setVisible(false);
				wConnect.setVisible(true);
				wConnect.setTfdUsername("");
			}
		}
	}

/**
 * This method is called whenever the ModelText object is changed.
 * update automatically the view when the user receive a message
 * @see chatSystemController.ModelText
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
 * @see chatSystemController.ModelListUsers
 * @param arg1
 */
	public void updateModelListUsers(Object arg1){
		if (this.mode == 0){
			this.notifylUsersChanges();
			this.cmd.setUsers(((HashMap<?, ?>)arg1 ).keySet().toArray());
		}else{
			System.out.println("modelList modifier");
			this.wListUsers.setUsers(((HashMap<?, ?>)arg1 ).keySet().toArray()) ;
		}
	}
	
	
/**
 * This method is called whenever the ModelFileToReceive object is changed.
 * update automatically the view when the user receive a File
 * @see chatSystemController.ModelFile
 * @param arg1
 */
	
	public void updateModelFileReceive(String arg0){ 
		//if (this.mode == 0){

		//}else{
	//	String remote=new String((String)arg0);
		System.out.println("dans update filereceived");
		this.wCommunicate.get(arg0).getBtnFileReceived().setVisible(true);
		//}
	}
	/**
	 * This method is called whenever the ModelFileToSend object is changed.
	 * update automatically the view when the user receive a File
	 * @see chatSystemController.ModelFile
	 * @param arg1
	 */
	public void updateModelFileProgress(Object arg1){
		//if (this.mode == 0){

		//}else{
		
		
		String remote=new String(((ModelFileToSend)arg1).getRemote());
		int progress=((ModelFileToSend)arg1).getProgression();
		//System.out.println(progress);
		this.wCommunicate.get(remote).getProgressBarFile().setValue(progress);
		//this.wCommunicate.get(remote).getBtnFileReceived().setVisible(true);
		if(progress==100){
			this.wCommunicate.get(remote).getProgressBarFile().setVisible(false);
			this.wCommunicate.get(remote).getProgressBarFile().setValue(0);
		}
		
	}

/**
 * This method is called whenever the ModelUsername object is changed.
 * update automatically the view when the user connected
 * @see chatSystemController.ModelUsername
 * @param arg1
 */
	public void updateModelUsername(Object arg1){
		if (this.mode != 0){
			if (((String)arg1).equals("MODE_DEBUG")){
				this.mode = 0;
				this.cmd = new CommandLine(this);
				this.cmd.initialize();
			}else{
				if(this.wListUsers==null){//premiere connection on cree la liste users
					this.wListUsers=new InterfaceListUsers(((String)arg1), this);
				}else {
					this.wListUsers.setLblUsername((String)arg1);
				}
				wConnect.setVisible(false);
				this.wListUsers.setVisible(true);
			}
		}
	}

/**
 * This method is called whenever the observed object is changed. 
 * An application calls an Observable object's notifyObservers method to have all the object's observers notified of the change. 
 * @param arg0
 * @param arg1
 */
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Dans update");
		if (arg0 instanceof ModelStates){
			this.updateModelStates(arg1);
		}else if(arg0 instanceof ModelText){
			this.updateModelText(arg0,arg1);
		}else if(arg0 instanceof ModelListUsers){
			this.updateModelListUsers(arg1);
		}else if(arg0 instanceof ModelUsername){
			this.updateModelUsername(arg1);
		}else if(arg0 instanceof ModelFileToSend){
			//System.out.println("Dans update fileToSend");
			this.updateModelFileProgress(arg0);
		}

	}


/**
 * is called when the user pushes connect button or presses enter
 */

	public void connect() {
		if (this.mode == 0){
			this.controller.performConnect(this.cmd.getUsername());
		}else{
			this.controller.performConnect(this.wConnect.getTfdUsername().trim());
		}
	}


/**
 * is called when the user pushes disconnect button
 */
	public void disconnect() {
		controller.performDisconnect();
	}

/**
 * is called when the user pushes send message button
 * @param remoteUsername 
 */
	public void sendMessage(String remoteUsername) {
		if(this.mode == 0){
			this.controller.performSendText(remoteUsername, this.cmd.getTextToSend());
		}else{
			String localUsername=this.wListUsers.getLblUsername();
			String text2Send =this.wCommunicate.get(remoteUsername).gettAreaMessageText().trim();
			this.wCommunicate.get(remoteUsername).settAreaHistoryCom(localUsername+" :"+text2Send+"\n");

			controller.performSendText(remoteUsername,wCommunicate.get(remoteUsername).gettAreaMessageText().trim());
			this.wCommunicate.get(remoteUsername).settAreaMessageText("");

		}

	}
/**
 * is called when the user pushes Join File button
 * @param remote
 * 			a remote username
 */
	public void sendFile(String remote) {
		this.openInterfaceDialogFile();
		if (InterfaceDialogFile.getDialogue().showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			String filePath=InterfaceDialogFile.getDialogue().getSelectedFile().getPath();
			this.wCommunicate.get(remote).getProgressBarFile().setVisible(true);
			this.controller.performPropositionFile(remote, filePath);
			System.out.println("dans sendFile dans chatGui");
			//this.wCommunicate.get(remote).getProgressBarFile().setVisible(false);
		}


	}

/**
 * is called when the user responds to a receive file demand
 * @param remote
 * 			a remote username
 * @param answer
 * 			answer of the user
 */
	public void receiveFile(String remote, boolean answer) {	
		this.controller.performFileAnswer(remote, answer);
	}

/**
 * 
 */
	public void addRecipient(String remote) {
		this.controller.performAddURecipient(remote);
	}

/**
 * 
 */
	public void removeRecipient(String remote) {
		this.controller.performRemoveRecipient(remote);
	}

/**
 * open a conversion window
 * @param remoteUsername
 */
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
	public void displayMessage(String text, String remote) {
		if (this.mode == 0){
			this.cmd.displayMessage(text, remote);
		}else{
			System.out.println("dans display");
			this.openWindowCommunicate(remote);
			this.wCommunicate.get(remote).settAreaHistoryCom("   "+remote+" :"+text+"\n");
		}	
	}

/**
 * 
 */
	public void notifyRemoteDisconnection(String remote) {
		if (this.mode == 0){
			this.cmd.displayRemoteDisconnection(remote);
		}else{

		}	
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
	public void proposeFile(String remote, String file, long size) {
		// TODO Auto-generated method stub
		String title=new String("Download File Proposition");
		String message=new String(remote+" vous envoi ce fichier "+file+" de taille "+size);
		this.openWindowCommunicate(remote);
		int answer = JOptionPane.showConfirmDialog(this.wCommunicate.get(remote), message, title,JOptionPane.YES_NO_OPTION);
		System.out.println("before perform file answer");
		if (answer == JOptionPane.YES_OPTION) {
			System.out.println("après perform file answer");
			this.controller.performFileAnswer(remote, true);        	
		}
		else if (answer == JOptionPane.NO_OPTION){
			this.controller.performFileAnswer(remote, false);
		}
	}

/**
 * 
 */
	public void notifyAnswerFile(String remote, String file, boolean answer) {
		if (this.mode == 0){
			this.cmd.displayFileAnswer(remote, file, answer);
		}else{

		}
	}

/**
 * 
 */
	public void displayCancelFile(String remote, String file) {

	}

/**
 * opens window which allows the user to select file in his local disk 
 */
	public void openInterfaceDialogFile() {
		this.wDialogFile=new InterfaceDialogFile();
	}

/**
 * initializes the connection window
 */
	public void initConnection() {
		this.mode = 1;
		this.wCommunicate=new HashMap<String,InterfaceCommunicate>();
		this.wConnect = new InterfaceConnect(this);
		this.wListUsers=new InterfaceListUsers("",this);
	}

/**
 * 
 */
	public void closeConnection() {

	}

/**
 * 
 */
	public void notifylUsersChanges() {
		if (this.mode == 0){
			System.out.println("List of users has changed");
		}else{

		}	
	}


	/* (non-Javadoc)
	 * @see chatSystemIHMs.ToUser#notifyRemoteConnection(java.lang.String)
	 */
	@Override
	public void notifyRemoteConnection(String remote) {
		

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