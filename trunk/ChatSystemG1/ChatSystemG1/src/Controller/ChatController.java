package Controller;

import chatSystemCommon.*;
import ChatNI.ChatNetwork;
import IHM.FrontController;

public class ChatController {
	private FrontController ChatGuiFrontController;
	private ChatNetwork ChatNi;
	private ChatModel ChatMod;
	private static String localUsername;
	
	public ChatController() {
		// TODO Auto-generated constructor stub
	}
	
	public static void ByeProcessing(Goodbye message){
		
	}
	
	public static void FileAcceptanceProcessing(Message message){
		
	}
	
	public static void FileProcessing(File message){
		
	}
	
	public static void HelloProcessing(Hello message){
		
	}
	
	public static void MessageProcessing(Text message){
		
	}
	public static void FileTransfertDemandProcessing(FileTransfertDemand message){
		
	}
	public void PerformConnect(){
		ChatNi.SendHello(false,getLocalUsername());
	}
	
	public void PerformDisconnect(){
		ChatNi.SendBye();
	}
	
	public void PerformFileAcceptance(File f,String RemoteUsername){
		
	}
	
	public void PerformSendFile(File f,String RemoteUserName){
		
	}
	
	public void PerformSendMessage(String text,String RemoteUserName){
		
	}
	
	public void UpdateModel(Message m){
		
	}
	
	public static String extractIpFromUserName(String Username){
		String retour = null;
		for(int i = 0; i <Username.length();i++ ){
			if(Username.charAt(i) == '@'){
				retour = Username.substring(i);
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
	 */
	public void setLocalUsername(String localUsername) {
		this.localUsername = localUsername;
	}
}
