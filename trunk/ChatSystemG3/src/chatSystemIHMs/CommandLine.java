/**
 * 
 */
package chatSystemIHMs;

import java.util.ArrayList;
import java.util.Scanner;

import chatSystemController.Controller;

/**
 * @author joanna
 *
 */
public class CommandLine implements FromUser,ToUser{

	private Controller controller;
	private Scanner sc;
	private ArrayList<String> users;
	private ArrayList<Boolean> lMRecipients; // list of recipients when multiple recipients
	private boolean toMultiple;
	private String textToSend; 
	
	
	CommandLine(Controller controller){
		this.controller = controller;
		this.sc = new Scanner(System.in);
		this.users = new ArrayList<String> ();
		this.toMultiple = false;
	}

	void displayDisconnectedMenu(){
		System.out.println("0 : Leave the chat (bad idea =/)");
		System.out.println("1 : Connect (excellent option ^^)");
	}
	
	
	void displayConnectedMenu(){
		System.out.println("0 : Leave the chat");
		System.out.println("1 : Send a message to one person");
		System.out.println("2 : Send a message to many people");
		System.out.println("3 : Send a file to one person");
		System.out.println("4 : Disconnect");
		this.chooseConnectOption();
	}
	
	
	void displaySendMenu(){
		System.out.println("1 : Send");
		System.out.println("2 : Cancel");
		System.out.println("3 : Add somebody to the conversation");
	}
	
	
	int getChoice(){
		Scanner n = new Scanner(System.in);
		int j = n.nextInt();
		return j;
	}
		
	
	void chooseConnectOption(){
		int j = this.getChoice();
		if (j == 0){
			System.exit(0);
		}else if (j == 1){
			this.sendMessage();
		}else if (j == 2){
			this.toMultiple = true;
			this.sendMessage();
			this.toMultiple = false;
		}else if (j == 3){
			this.sendFile();
		}else if (j == 4){
			this.disconnect();
		}else { System.out.println( j + "is not a number between 0 and 4 =/, try again"); }
	}
	
	
	void chooseDisconnectOption(){
		int choice;
		choice = this.getChoice();
		if (choice == 1){ 
			this.connect();
		}else { System.exit(0); }
	}
	
	
	void chooseSendOption(){
		int j = this.getChoice();
		if (j == 1){
			this.communicateRecipientsToController();
			this.send();
		}else if (j == 2){
			this.reinitializeMsg();
			this.displayConnectedMenu();
		}else if (j == 3){
			this.addRecipient();
		}else { System.out.println( j + "is not a number between 0 and 4 =/, try again"); }
	}
	
	
	public void connect() {
		System.out.println("Enter a surname");
		String username = this.sc.nextLine();
		this.controller.performConnect(username);
		this.displayConnectedMenu();
	}


	public void disconnect() {
		this.controller.performDisconnect();
		System.out.println("You are disconnected");
		this.displayDisconnectedMenu();
	}

	
	private void displayRemotes(){
		int i = 1;		
		// affichage des utilisateurs connectés
		System.out.println("Here is the liste of connected users");
		while (this.users.iterator().hasNext()){
			System.out.println( i + " : " +(String)this.users.iterator().next());
			i++;
		}
	}
	
	
	private int defineRecipients(){
		int i = 1;
		int numberOfRecipients = 0;
		this.lMRecipients = new ArrayList<Boolean>(this.users.size());
		
		// i designe l'indice du destinataire
		i = -1;
		// choix du ou des destinataires
		if (this.toMultiple == true){
			while (i != 0){
				System.out.println("Select the recipient's number (0 if no more recipient): ");
				i = sc.nextInt();
				if (i <= this.users.size());
					this.addOne(i);
				numberOfRecipients++;
			}			
		}else{
			System.out.println("Select a recipient (tape his number)");
			i = sc.nextInt();
			if (i <= this.users.size())
				this.addOne(i);
			numberOfRecipients++;
		}
		return numberOfRecipients;
	}
	
	
	private void addOne(int recipient){
		if (this.lMRecipients.get(recipient) == false){
			this.lMRecipients.add(recipient, true);
			System.out.println("ajouté");
		}else{
			System.out.println("This user is already registered");
		}
	}
	
	
	public void addRecipient() {
		// choix du ou des destinataires
		this.defineRecipients();
	}
	
		
	private void writeMsg(){
		System.out.println("Enter your message (end by entry)");
		this.textToSend = sc.nextLine();
	}
	
	private void communicateRecipientsToController(){
		int i;
		// partage de la liste des destinataires avec le controller 
		for (i=0;i<this.lMRecipients.size();i++){			
			if (this.lMRecipients.get(i) == true){
				this.controller.performAddURecipient(this.users.get(i));
			}
		}
	}
	
	private void send(){
		// envoi du message
		//this.controller.performSendText(textToSend);
		System.out.println("message transmitted");
	}
	
	private void reinitializeMsg(){
		this.lMRecipients=null;
		this.textToSend=null;
	}
	
	public void sendMessage() {
		int nOfRecipients;
		// affichage de la liste des personnes connectées
		this.displayRemotes();
		// choix du ou des destinataires
		nOfRecipients = this.defineRecipients();	
		if (nOfRecipients > 0){
			// ecriture du message
			this.writeMsg();
			this.displaySendMenu();
			this.chooseSendOption();
		}else { 
			System.out.println("Not able to choose a number between 0 and 4... what a shame u_u"); 
			System.exit(0);
		}
	}


	public void sendFile() {}


	public void initConnection() {
		System.out.println("Welcome !!!");
		this.displayDisconnectedMenu();	
		this.chooseDisconnectOption();
	}
	
	
	public void displayMessage(String text, String remote) {
		System.out.println(remote + " : " + text);		
	}


	public void notifyRemoteConnection(String remote) {
		System.out.println(remote + "is connected");			
	}


	public void notifyRemoteDisconnection(String remote) {
		System.out.println(remote + "is disconnected");
	}

	/* (non-Javadoc)
	 * @see chatSystemIHMs.ToUser#openWindowCommunicate(java.lang.String)
	 */
	@Override
	public void openWindowCommunicate(String RemoteUsername) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see chatSystemIHMs.FromUser#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String user) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see chatSystemIHMs.ToUser#proposeFile(java.lang.String, java.lang.String, long)
	 */
	@Override
	public void proposeFile(String remote, String file, long size) {
		// TODO Auto-generated method stub
		
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
		
	}

	/* (non-Javadoc)
	 * @see chatSystemIHMs.FromUser#sendFile(java.lang.String)
	 */
	@Override
	public void sendFile(String remote) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see chatSystemIHMs.ToUser#openOngletDialog()
	 */
	
	
}