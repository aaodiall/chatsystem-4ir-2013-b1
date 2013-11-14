package Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import chatSystemCommon.*;
import ChatNI.ChatNetwork;
import IHM.FrontController;

public class ChatController {
	private static final boolean from = true;
	private static final boolean to = false;
	
	private FrontController ChatGuiFrontController;
	private static boolean isConnected ;
	private static ChatNetwork ChatNi;

	private static ChatModel ChatMod;
	private static String localUsername;
	
	public ChatController() {
		// TODO Auto-generated constructor stub
		isConnected = false;
		ChatNi = null;
		localUsername = null;
		ChatMod = new ChatModel();
		ChatGuiFrontController = new FrontController();
		ChatMod.registerObserver(ChatGuiFrontController);
	}
	
	/**
	 * @return the isConnected
	 */
	public static boolean isConnected() {
		return isConnected;
	}

	public static void ByeProcessing(Goodbye message){
		System.out.println("on a reçu un bye" + message);
		if(ChatMod.getUserNameList().contains(message.getUsername())){
			ChatMod.RemoveUser(message.getUsername());
		}
	}
	
	public static void FileAcceptanceProcessing(FileTransfertConfirmation message){
		if(message.isAccepted()){
			System.out.println("yahou ils ont accepté!" + message);
			
		}else{
			System.out.println("oh non ils ont refusé" + message);
		}
	}
	
	public static void FileProcessing(File message){
		
	}
	
	public static void HelloProcessing(Hello message){
		if(message.isAck()){
			System.out.println("on a recu un ack" + message);
		}else{
			
			
			System.out.println("on a recu un hello" + message);
			ChatNi.SendHello(true,message.getUsername());
			
		}
		if(!ChatMod.getUserNameList().contains(message.getUsername())){
			ChatMod.addUser(message.getUsername());
		}
	}
	
	public static void MessageProcessing(Text message){
		String[] remote = new String[1];
		remote[0] = message.getUsername();
		UpdateModel(message, remote,from	);

		System.out.println("on a recu un message" + message);
	}
	public static void FileTransfertDemandProcessing(FileTransfertDemand message){
		System.out.println("on a recu une ftd" + message);
		//demand de transfert
		boolean transfertacceptance = false;
		FileTransfertConfirmation ftco = new FileTransfertConfirmation(getLocalUsername(),transfertacceptance, message.getId());
		
			
		ChatNi.SendFileTransfertConfirmation(ftco, message.getUsername());
		
	}
	public static void PerformConnect(){
		isConnected = true;
		ChatNi = new ChatNetwork();
		ChatNi.SendHello(false,null);
	}
	
	public static void PerformDisconnect(){
		isConnected = false;
		ChatNi.SendBye();
		ChatNi.getMessageReceiver().closeReceiveSocket();
		ChatNi.getMessageSender().closeSendSocket();
	}
	
	public void PerformFileAcceptance(java.io.File f,String RemoteUsername){
		double bytes = f.length();
		String name = f.getName();
		String extension = null;
		for(int i = 0; i <name.length();i++ ){
			if(name.charAt(i) == '.'){
				extension = name.substring(i+1);
			}
		}
		
		FileTransfertDemand ftd = new FileTransfertDemand(getLocalUsername(), name, extension,(int) bytes);
		ChatNi.SendFileAcceptance(ftd,RemoteUsername);
	}
	
	public void PerformSendFile(java.io.File f,String RemoteUserName){
		
	}
	
	public static void PerformSendMessage(String text,String[] RemoteUserName){
		Text message = new Text(getLocalUsername(),text);
		UpdateModel(message, RemoteUserName,to	);
		System.out.println("on envoie un message");
		ChatNi.SendMessage(message, RemoteUserName);
	}
	
	
	
	public static String extractIpFromUserName(String Username){
		String retour = null;
		for(int i = 0; i <Username.length();i++ ){
			if(Username.charAt(i) == '@'){
				retour = Username.substring(i+1);
			}
		}
		return retour;
	}
	
	public String GetUserNameFromIp(String RemoteIp){
		String retour = null;
		for(Conversation c : ChatMod.getConversationList()){
			for(String s : c.getUserNameList()){
				if(s.contains(RemoteIp)){
					retour = s;
				}
			}
		}
		
		
		return retour;
		
	}
	/**
	 * @return the localUsername
	 */
	public static String getLocalUsername() {
		return localUsername;
	}

	/**
	 * @param localUsername the localUsername to set
	 * @throws UnknownHostException 
	 */
	public static void setLocalUsername(String localusername) throws UnknownHostException {
		localUsername = localusername+"@"+InetAddress.getLocalHost().getHostAddress().toString();
	}
	/**
	 * @return the chatGuiFrontController
	 */
	public FrontController getChatGuiFrontController() {
		return ChatGuiFrontController;
	}

	/**
	 * @return the chatNi
	 */
	public ChatNetwork getChatNi() {
		return ChatNi;
	}

	/**
	 * @return the chatMod
	 */
	public ChatModel getChatMod() {
		return ChatMod;
	}

	/**
	 * @param chatGuiFrontController the chatGuiFrontController to set
	 */
	public void setChatGuiFrontController(FrontController chatGuiFrontController) {
		ChatGuiFrontController = chatGuiFrontController;
	}

	/**
	 * @param chatNi the chatNi to set
	 */
	public void setChatNi(ChatNetwork chatNi) {
		ChatNi = chatNi;
	}

	/**
	 * @param chatMod the chatMod to set
	 */
	public void setChatMod(ChatModel chatMod) {
		ChatMod = chatMod;
	}

	public static void FileCancelProcessing(Message message) {
		// TODO Auto-generated method stub
		
	}

	public static ArrayList<String> getUserList() {
		// TODO Auto-generated method stub
		return ChatMod.getUserNameList();
	}
	
	public static void UpdateModel(Message m,String[] Remote, boolean from_to){
		int numConv;
		if(from){
			Conversation t = null;
			boolean exist = false;
			for(Conversation c : ChatMod.getConversationList()){
				if(c.getUserNameList().get(0).equals(Remote[0])){
					t = c;
					exist = true;
				}
			}
			if(!exist){
				ArrayList<String> usa = new ArrayList<String>();
				usa.add(Remote[0]);
				t = new Conversation(usa);
				
				ChatMod.getConversationList().add(t);
			}
			SignedAndDatedMessage sadm = new SignedAndDatedMessage(new Date(),((Text) m).getText(), Remote[0]);
			t.getMessageList().add(sadm);
			numConv = ChatMod.getConversationList().indexOf(t) + 1;
		}else{
			Conversation t = null;
			boolean exist = false;
			for(Conversation c : ChatMod.getConversationList()){
				if(c.getUserNameList().get(0).equals(Remote[0])){
					t = c;
					exist = true;
				}
			}
			if(!exist){
				ArrayList<String> usa = new ArrayList<String>();
				usa.add(Remote[0]);
				t = new Conversation(usa);
				
				ChatMod.getConversationList().add(t);
			}
			SignedAndDatedMessage sadm = new SignedAndDatedMessage(new Date(),((Text) m).getText(), getLocalUsername());
			t.getMessageList().add(sadm);
			numConv = ChatMod.getConversationList().indexOf(t) + 1;

		}
		ChatMod.notifyObserver(numConv);
	}

	public static Conversation getConv(int notif) {
		// TODO Auto-generated method stub
		return ChatMod.getConversationList().get(notif);
	}
}
