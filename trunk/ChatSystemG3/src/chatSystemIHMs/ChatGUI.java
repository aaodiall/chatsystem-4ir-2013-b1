/**
 * 
 */
package chatSystemIHMs;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

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
	private final Map<String,InterfaceCommunicate > wCommunicate;
	private InterfaceListUsers wListUsers=null;;
	private CommandLine cmd;
	private InterfaceDialogFile wDialogFile;
	private Controller controller;
	// private final Map<String, InterfaceCommunicate> wCommunicate;
	//private ToUser toUser;
	//private FromUser fromUser;


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
		/*Scanner sc = new Scanner(System.in);
		System.out.println("Choose the mode : 1 for graphical , 0 for command line");
		int a = sc.nextInt();
		if (a == 1){*/
		this.wCommunicate=new HashMap<String,InterfaceCommunicate>();
		this.controller=controller;
		this.wConnect = new InterfaceConnect(this);
		this.wListUsers=new InterfaceListUsers("",this);
		//this.wCommunicate = new HashMap<String, InterfaceCommunicate>();
		/*}else{
			this.cmd = new CommandLine(controller);
			this.cmd.initConnection();
		}*/



	}


	public void cmdUpdate(Observable arg0, Object arg1){
		if(arg0.getClass()==ModelListUsers.class){

		}else if(arg0.getClass().equals(ModelUsername.class)){

		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

		if (arg0 instanceof ModelStates){
			if ((Boolean)arg1.equals(false)){
			//	wCommunicate.setVisible(false);
				this.wListUsers.getUsers().clear();
				this.wCommunicate.clear();
				this.wListUsers.setVisible(false);
				wConnect.setVisible(true);
				wConnect.setTfdUsername("");
			}
		}
		if(arg0 instanceof ModelText){
		//	arg1=new ModelText();
			String txtReceived=new String(((ModelText)arg0).getTextReceived());
			String remote=new String(((ModelText)arg0).getRemote());
			//System.out.println(remote);
			this.displayMessage(txtReceived,remote);
			
			//wCommunicate.settAreaHistoryCom(wCommunicate.gettAreaHistoryCom()+"\n"+((String)arg1));
			
		}


		if(arg0 instanceof ModelListUsers){
			System.out.println("modelList modifier");
			this.wListUsers.setUsers(( (HashMap<?, ?>)arg1 ).keySet().toArray()) ;

			//	wCommunicate.setUsers(( (HashMap<?, ?>)arg1 ).keySet().toArray()) ;
		}
		if(arg0 instanceof ModelUsername){//arg0.getClass().equals(ModelUsername.class
			if(this.wListUsers==null){//premiere connection on cree la liste users
				this.wListUsers=new InterfaceListUsers(((String)arg1), this);
			}else {
				this.wListUsers.setLblUsername((String)arg1);
			}

			wConnect.setVisible(false);
			this.wListUsers.setVisible(true);

		

		//	wCommunicate.setVisible(true);
		//wCommunicate.setLblUsername(((String)arg1));
	}
	//ainsi de suite
}

/* (non-Javadoc)
 * @see chatSystemIHMs.FromUser#connect()
 */
@Override
public void connect() {
	// TODO Auto-generated method stub
	controller.performConnect(wConnect.getTfdUsername());


}
/* (non-Javadoc)
 * @see chatSystemIHMs.FromUser#disconnect()
 */
@Override
public void disconnect() {
	// TODO Auto-generated method stub
	
	controller.performDisconnect();
}
/* (non-Javadoc)
 * @see chatSystemIHMs.FromUser#sendMessage()
 */
@Override
public void sendMessage(String remoteUsername) {
	// TODO Auto-generated method stub
	String localUsername=this.wListUsers.getLblUsername();
	String text2Send =this.wCommunicate.get(remoteUsername).gettAreaMessageText();
	this.wCommunicate.get(remoteUsername).settAreaHistoryCom(localUsername+" :"+text2Send+"\n");
	controller.performSendText(remoteUsername,wCommunicate.get(remoteUsername).gettAreaMessageText());
	this.wCommunicate.get(remoteUsername).settAreaMessageText("");
	
}
/* (non-Javadoc)
 * @see chatSystemIHMs.FromUser#sendFile()
 */
@Override
public void sendFile(String remote) {
	// TODO Auto-generated method stub
	this.openInterfaceDialogFile();
	String filePath=InterfaceDialogFile.getDialogue().getSelectedFile().getPath();
    this.controller.performPropositionFile(remote, filePath);

}
/* (non-Javadoc)
 * @see chatSystemIHMs.FromUser#addRecipient()
 */
@Override
public void addRecipient() {
	// TODO Auto-generated method stub

}
/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#initConnection()
 */
@Override
public void initConnection() {
	// TODO Auto-generated method stub


}
/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#openOngletDialog()
 */
@Override
public void openWindowCommunicate(String remoteUsername) {
	// TODO Auto-generated method stub
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
/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#displayMessage(java.lang.String, java.lang.String)
 */
@Override
public void displayMessage(String text, String remote) {
	// TODO Auto-generated method stub
	System.out.println("dans display");
	this.wCommunicate.get(remote).settAreaHistoryCom("   "+remote+" :"+text+"\n");

}
/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#notifyRemoteConnection(java.lang.String)
 */
@Override
public void notifyRemoteConnection(String remote) {
	// TODO Auto-generated method stub

}
/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#notifyRemoteDisconnection(java.lang.String)
 */
@Override
public void notifyRemoteDisconnection(String remote) {
	// TODO Auto-generated method stub

}


/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#proposeFile(java.lang.String, java.lang.String, long)
 */
@Override
public void proposeFile(String remote, String file, long size) {
	// TODO Auto-generated method stub
	String title=new String("Download File Proposition");
	String message=new String(remote+" vous envoi ce fichier "+file+" de taille "+size);
	this.openWindowCommunicate(remote);
	int answer = JOptionPane.showConfirmDialog(this.wCommunicate.get(remote), message, title,JOptionPane.YES_NO_OPTION);
	if (answer == JOptionPane.YES_OPTION) {
		this.controller.performFileAnswer(remote, true);
	}
	else if (answer == JOptionPane.NO_OPTION){
		this.controller.performFileAnswer(remote, false);
	}
}


/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#displayOkFile(java.lang.String, java.lang.String)
 */
@Override
public void displayOkFile(String remote, String file) {
	// TODO Auto-generated method stub
	
}


/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#displayKoFile(java.lang.String, java.lang.String)
 */
@Override
public void displayKoFile(String remote, String file) {
	// TODO Auto-generated method stub
	
}


/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#displayCancelFile(java.lang.String, java.lang.String)
 */
@Override
public void displayCancelFile(String remote, String file) {
	// TODO Auto-generated method stub
	
}


/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#openInterfaceDialogFile()
 */
@Override
public void openInterfaceDialogFile() {
	// TODO Auto-generated method stub
	this.wDialogFile=new InterfaceDialogFile();
}


/* (non-Javadoc)
 * @see chatSystemIHMs.ToUser#openWindowCommunicate()
 */







}