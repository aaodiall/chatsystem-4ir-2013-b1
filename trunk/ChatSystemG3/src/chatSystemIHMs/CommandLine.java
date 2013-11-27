/**
 * 
 */
package chatSystemIHMs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


import chatSystemController.Controller;

/**
 * @author joanna
 *
 */
public class CommandLine {

	private ChatGUI chatgui;
	private Scanner sc;
	private HashSet<String> users;
	private ArrayList<Boolean> lMRecipients; // list of recipients when multiple recipients
	private boolean toMultiple;
	private String textToSend; 
	private String username;
	private String nameFile;
	
	
	CommandLine(ChatGUI chatgui){
		this.chatgui = chatgui;
		this.username = new String();
		this.sc = new Scanner(System.in);
		this.users = new HashSet<String> ();
		this.toMultiple = false;
	}

	
	String getUsername(){
		return this.username;
	}
	
	
	String getTextToSend(){
		return this.textToSend;
	}
	
	
	boolean getToMultiple(){
		return this.toMultiple;
	}
	
	void setUsers(Object[] lusers){
		// phase 1 on ajoute tous le monde pour avoir la liste la plus complete possible
		for(int i=0;i<lusers.length;i++){
			this.users.add((String)lusers[i]);			
		}
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
	}
	
	
	void displaySendMenu(){
		System.out.println("1 : Send");
		System.out.println("2 : Cancel");
		System.out.println("3 : Remove somebody to the conversation");
	}
	
	/**
	 * 
	 * @return le choix de l'utilisateur sous la forme d'un nombre
	 */
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
			//this.communicateRecipientsToGUI();
			this.send();
			this.reinitializeMsg();
		}else if (j == 2){
			this.reinitializeMsg();
			this.displayConnectedMenu();
			this.chooseConnectOption();
		}else if (j == 3){
			this.removeRecipient();
		}else { System.out.println( j + "is not a number between 0 and 4 =/, try again"); }
	}
	
	
	public void connect() {
		System.out.println("Enter a surname");
		this.username = this.sc.nextLine();
		this.chatgui.connect();
		this.displayRemotes();
		this.displayConnectedMenu();
		this.chooseConnectOption();
	}


	public void disconnect() {
		this.chatgui.disconnect();
		System.out.println("You are disconnected");
		this.displayDisconnectedMenu();
		this.chooseDisconnectOption();
	}

	
	private void displayRemotes(){
		if (this.users.size() > 0){
			int i = 1;		
			// affichage des utilisateurs connectés
			System.out.println("Here is the liste of connected users");
			while (this.users.iterator().hasNext()){
				System.out.println( i + " : " +(String)this.users.iterator().next());
				i++;
			}
		}else{
			System.out.println("No connected people, back the main menu");
			this.displayConnectedMenu();
			this.chooseConnectOption();
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
			System.out.println("added");
		}else{
			System.out.println("This user is already registered");
		}
	}
	
		
	private void displayRecipients(){
		int i;
		for (i=1;i<=this.lMRecipients.size();i++){
			System.out.println(i + " : " +this.lMRecipients.get(i));
		}
	}
	
	
	private void removeRecipient(){
		if (this.lMRecipients.size() > 0){
			this.displayRecipients();
			System.out.println("Entrez le nom de la personne à retirer de la liste");
			int choice = this.getChoice();
			if ((choice > 0) && (choice <= this.lMRecipients.size())){
				this.lMRecipients.remove(choice);
			}
			this.displaySendMenu();
			this.chooseSendOption();
		}else{
			System.out.println("Pas de destinataire pour le moment");
		}
	}
	
	
	private void writeMsg(){
		System.out.println("Enter your message (end by entry)");
		this.textToSend = sc.nextLine();
	}
	
	
	/*private void communicateRecipientsToGUI(){
		int i;
		// partage de la liste des destinataires avec le controller 
		for (i=0;i<this.lMRecipients.size();i++){			
			if (this.lMRecipients.get(i) == true){
				this.chatgui.addRecipient(this.users.get(i));
			}
		}
	}*/
	
	
	private void send(){
		int i;
		// partage de la liste des destinataires avec le controller 
		for (i=0;i<this.lMRecipients.size();i++){			
			if (this.lMRecipients.get(i) == true){
				this.chatgui.sendMessage((String)this.users.toArray()[i]);
			}
		}
		// envoi du message
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


	public void sendFile() {
		// choisir le fichier
		//choisir le destinataire
		//envoyer la proposition
	}
	
	public void initialize(){
		System.out.println("Welcome !!!");
		this.displayDisconnectedMenu();	
		this.chooseDisconnectOption();
	}
	
	
	public void displayMessage(String text, String remote) {
		System.out.println(remote + " : " + text);		
	}


	public void displayRemoteConnection(String remote) {
		System.out.println(remote + "is connected");			
	}


	public void displayRemoteDisconnection(String remote) {
		System.out.println(remote + "is disconnected");
	}

	
	public void displayPropositionFile(String remote, String fileName){
		System.out.println("Do you want the file : ");
		System.out.println(fileName);
		System.out.println("from " + remote + "? (0 : no, 1 : yes)");
		if (this.getChoice() == 1){
			this.chatgui.receiveFile(remote, true);
		}else{
			this.chatgui.receiveFile(remote, false);
		}
	}
	
	
	public void displayFileAnswer(String remote,String fileName,boolean answer){
		if (answer == true){
			System.out.println(remote + "has accepted the file : ");
		}else{
			System.out.println(remote + "has refused the file : ");
		}
		System.out.println(fileName);
	}
	
}