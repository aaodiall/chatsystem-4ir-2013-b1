package Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import chatSystemCommon.*;
import ChatNI.ChatNetwork;
import IHM.FrontController;

public class ChatController {
	private FrontController ChatGuiFrontController;
	private static boolean isConnected ;
	private static ChatNetwork ChatNi;

	private ChatModel ChatMod;
	private static String localUsername;
	
	public ChatController() {
		// TODO Auto-generated constructor stub
		isConnected = false;
		ChatNi = null;
		localUsername = null;
	}
	
	/**
	 * @return the isConnected
	 */
	public static boolean isConnected() {
		return isConnected;
	}

	public static void ByeProcessing(Goodbye message){
		System.out.println("on a reçu un bye" + message);

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
	}
	
	public static void MessageProcessing(Text message){
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
	
	public void PerformSendMessage(String text,String[] RemoteUserName){
		Text message = new Text(getLocalUsername(),text);
		System.out.println("on envoie un message");
		ChatNi.SendMessage(message, RemoteUserName);
	}
	
	public void UpdateModel(Message m){
		
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

}
