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
	private static ModelStates modelStates;
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
	
	public static ModelStates getModelStates(){
		return modelStates;
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
		chatController = new Controller();
		chatNI = new ChatNI(16001);
		modelListUsers = new ModelListUsers();
		modelUsername = new ModelUsername();
		modelStates = new ModelStates();
		modelGroupRecipient = new ModelGroupRecipient();
		modelText = new ModelText();
		chatController.performConnect("lilou");
		remote.add((String)(ChatSystem.getModelListUsers().getListUsers().keySet().iterator().next()));
		chatController.performSendText("premier message",remote);
		/*chatController.performDisconnect();
		if (modelStates.isConnected()){
			System.out.println(modelUsername.getUsername() + " : disconnection succeed");
		}else{
			System.out.println(modelUsername.getUsername() + " : disconnection failed");
		}*/
	}

}
