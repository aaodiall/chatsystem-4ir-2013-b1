/**
 * 
 */
package chatSystemIHMs;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import javax.swing.JOptionPane;
import chatSystemController.Controller;
import chatSystemModel.ModelListUsers;
import chatSystemModel.ModelStates;
import chatSystemModel.ModelText;
import chatSystemModel.ModelUsername;

/**
 * @author alpha
 *
 */
//a faire: ouvrir un dialogue bloquand pour la demande de download file
//penser a barre de progression
//regler probleme exception dans file propositionReceived	
public class ChatGUI extends View implements Observer,ToUser,FromUser{
	private InterfaceConnect wConnect;
	private Map<String,InterfaceCommunicate > wCommunicate;
	private InterfaceListUsers wListUsers=null;;
	private CommandLine cmd;
	private InterfaceDialogFile wDialogFile;
	private Controller controller;
	private int mode; 



	/*
	public InterfaceConnect getwConnect() {
		return wConnect;
	}
	public InterfaceCommunicate getwCommunicate() {
		return wCommunicate;
	}*/
	
	/**
	 * @param wConnect
	 * @param wCommunicate
	 */
	public ChatGUI(Controller controller) {
		this.controller=controller;
		this.initConnection();
	}
	
	
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
	
	
	public void updateModelText(Observable arg0,Object arg1){
		String txtReceived=new String(((ModelText)arg0).getTextReceived());
		String remote=new String(((ModelText)arg0).getRemote());
		this.displayMessage(txtReceived,remote);

		//	arg1=new ModelText();
		//System.out.println(remote);
		//wCommunicate.settAreaHistoryCom(wCommunicate.gettAreaHistoryCom()+"\n"+((String)arg1));
	}
	
	
	public void updateModelListUsers(Object arg1){
		if (this.mode == 0){
			this.notifylUsersChanges();
			this.cmd.setUsers(((HashMap<?, ?>)arg1 ).keySet().toArray());
		}else{
			System.out.println("modelList modifier");
			this.wListUsers.setUsers(((HashMap<?, ?>)arg1 ).keySet().toArray()) ;
		}
	}
	
	public void updateModelUsername(Object arg1){
		if (this.mode != 0){
			if(this.wListUsers==null){//premiere connection on cree la liste users
				this.wListUsers=new InterfaceListUsers(((String)arg1), this);
			}else {
				this.wListUsers.setLblUsername((String)arg1);
			}
			wConnect.setVisible(false);
			this.wListUsers.setVisible(true);
		}
	}
	
	
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof ModelStates){
			this.updateModelStates(arg1);
		}else if(arg0 instanceof ModelText){
			this.updateModelText(arg0,arg1);
		}else if(arg0 instanceof ModelListUsers){
			this.updateModelListUsers(arg1);
		}else if(arg0 instanceof ModelUsername){
			this.updateModelUsername(arg1);
		}
		//	wCommunicate.setVisible(true);
		//wCommunicate.setLblUsername(((String)arg1));
	}

	//ainsi de suite
		

	public void connect() {
		if (this.mode == 0){
			this.controller.performConnect(this.cmd.getUsername());
		}else{
			this.controller.performConnect(this.wConnect.getTfdUsername());
		}
	}

	
	public void disconnect() {
		controller.performDisconnect();
	}

	
	public void sendMessage(String remoteUsername) {
		if(this.mode == 0){
			this.controller.performSendText(remoteUsername, this.cmd.getTextToSend());
		}else{
			String localUsername=this.wListUsers.getLblUsername();
			String text2Send =this.wCommunicate.get(remoteUsername).gettAreaMessageText();
			this.wCommunicate.get(remoteUsername).settAreaHistoryCom(localUsername+" :"+text2Send+"\n");
			controller.performSendText(remoteUsername,wCommunicate.get(remoteUsername).gettAreaMessageText());
			this.wCommunicate.get(remoteUsername).settAreaMessageText("");
		}
		
	}
	
	public void sendFile(String remote) {
		this.openInterfaceDialogFile();
		String filePath=InterfaceDialogFile.getDialogue().getSelectedFile().getPath();
	    this.controller.performPropositionFile(remote, filePath);
	
	}

	
	public void receiveFile(String remote, boolean answer) {	
		this.controller.performFileAnswer(remote, answer);
	}
	
	
	public void addRecipient(String remote) {
		this.controller.performAddURecipient(remote);
	}


	public void removeRecipient(String remote) {
		this.controller.performRemoveRecipient(remote);
	}
	
	
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
	
	public void displayMessage(String text, String remote) {
		if (this.mode == 0){
			this.cmd.displayMessage(text, remote);
		}else{
			System.out.println("dans display");
			this.wCommunicate.get(remote).settAreaHistoryCom("   "+remote+" :"+text+"\n");
		}	
	}

	
	public void notifyRemoteDisconnection(String remote) {
		if (this.mode == 0){
			this.cmd.displayRemoteDisconnection(remote);
		}else{
		
		}	
	}
	
	
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

	
	public void notifyAnswerFile(String remote, String file, boolean answer) {
		if (this.mode == 0){
			this.cmd.displayFileAnswer(remote, file, answer);
		}else{
		
		}
	}
	
	
	public void displayCancelFile(String remote, String file) {
		
	}
	
	
	public void openInterfaceDialogFile() {
		this.wDialogFile=new InterfaceDialogFile();
	}


	public void initConnection() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Choose the mode : 1 for graphical , 0 for command line");
		int a = sc.nextInt();
		if (a == 0){
			this.mode = 0;
			this.cmd = new CommandLine(this);
			this.cmd.initialize();
		}else{
			this.mode = 1;
			this.wCommunicate=new HashMap<String,InterfaceCommunicate>();
			this.wConnect = new InterfaceConnect(this);
			this.wListUsers=new InterfaceListUsers("alpha",this);
		}
	}


	public void closeConnection() {
		
	}


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
		// TODO Auto-generated method stub
		
	}
}