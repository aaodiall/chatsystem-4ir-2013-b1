package runChat;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import chatSystemModel.*;
import chatSystemNetwork.*;
import chatSystemController.Controller;
import chatSystemIHMs.*;

public class ChatSystem {

	
	private static Controller chatController;
	private static ChatGui chatGui;
	private static ChatNI chatNI;
	private static ModelListUsers modelListUsers;
	private static ModelUsername modelUsername;
	private static ModelStateConnected modelStateConnected ;
	private static ModelText modelText;
	private static ModelGroupRecipient modelGroupRecipient;
	
	public static Controller getController(){
		return chatController;
	}
	
	public static ChatGui getChatGui(){
		return chatGui;
	}
	
	public static ChatNI getChatNI(){
		return chatNI;
	}
	
	public static ModelListUsers getModelListUsers(){
		return modelListUsers; 
	}
	
	public static ModelUsername getModelUsername(){
		return modelUsername;
	}
	
	
	public static ModelText getModelText(){
		return modelText;
	}
	
	public static ModelGroupRecipient getModelGroupRecipient(){
		return modelGroupRecipient;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList <String> remote = new ArrayList<String>();
			
		
		
		modelListUsers = new ModelListUsers();
		modelUsername = new ModelUsername();
		modelStateConnected = new ModelStateConnected();
		modelGroupRecipient = new ModelGroupRecipient();
		modelText = new ModelText();
		chatController = new Controller(modelListUsers,modelStateConnected , modelText, modelUsername, modelGroupRecipient);
		chatGui=new ChatGui(chatController);
		chatNI = new ChatNI(16001,chatController);
		modelUsername.addObserver(chatNI);
		modelListUsers.addObserver(chatGui);
		modelStateConnected.addObserver(chatNI);
		modelGroupRecipient.addObserver(chatNI);
		System.out.println("alpha");
		//chatGui.getwConnect();
		/*chatController.performDisconnect();*/
	/*	if (modelStates.isConnected()){
		chatController.performConnect("lilou");*/
		remote.add((String)(ChatSystem.modelListUsers.getListUsers().keySet().iterator().next()));
		
		chatController.performSendText("premier message",remote);
		System.out.println(ChatSystem.getModelUsername().getUsername());
		/*chatController.performDisconnect();
		if (modelStates.isConnected()){
			System.out.println(modelUsername.getUsername() + " : disconnection succeed");
		}else{
			System.out.println(modelUsername.getUsername() + " : disconnection failed");
		}*/
	}

}