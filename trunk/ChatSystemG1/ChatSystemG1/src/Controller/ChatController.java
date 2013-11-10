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
	public void PerformConnect(String Username){
		ChatNi.SendHello(true,Username);
	}
	
	public void PerformDisconnect(){
		
	}
	
	public void PerformFileAcceptance(){
		
	}
	
	public void PerformSendFile(){
		
	}
	
	public void PerformSendMessage(){
		
	}
	
	public void UpdateModel(){
		
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
