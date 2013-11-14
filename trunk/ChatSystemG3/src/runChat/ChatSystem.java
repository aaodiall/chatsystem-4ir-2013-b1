package runChat;
import java.net.UnknownHostException;
import java.util.ArrayList;

import chatSystemModel.*;
import chatSystemNetwork.*;
import chatSystemController.Controller;
import chatSystemIHMs.*;

public class ChatSystem {
	
	private static Controller chatController;
	private static ChatGUI chatGUI;
	private static ChatNI chatNI;
	private static ModelListUsers modelListUsers;
	private static ModelUsername modelUsername;
	private static ModelStates modelStates;
	private static ModelText modelText;
	private static ModelGroupRecipient modelGroupRecipient;
	
	public static Controller getController(){ return chatController; }
	
	public static ChatGUI getChatGui(){	return chatGUI;	}
	
	public static ChatNI getChatNI(){ return chatNI; }
	
	public static ModelListUsers getModelListUsers(){ return modelListUsers; }
	
	public static ModelUsername getModelUsername(){ return modelUsername; }
	
	public static ModelStates getModelStates(){	return modelStates;	}
	
	public static ModelText getModelText(){	return modelText; }
	
	public static ModelGroupRecipient getModelGroupRecipient(){	return modelGroupRecipient; }
	
	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException{
		ArrayList <String> remote = new ArrayList<String>();
		int portUDP=16001;
		Thread chatNIThread;
		modelListUsers = new ModelListUsers();
		modelUsername = new ModelUsername();
		modelStates = new ModelStates();
		modelGroupRecipient = new ModelGroupRecipient();
		modelText = new ModelText();
		chatController = new Controller(modelListUsers,modelStates , modelText, modelUsername, modelGroupRecipient);
		chatGUI=new ChatGUI(chatController);
		chatNI = new ChatNI(portUDP,chatController);
		chatNIThread = new Thread(chatNI);
		chatNIThread.start();
		modelUsername.addObserver(chatNI);
		modelListUsers.addObserver(chatGUI);
		modelStates.addObserver(chatNI);
		modelGroupRecipient.addObserver(chatNI);
		//System.out.println("alpha");

		
		// TEST de CONNEXION 
		//chatController.performConnect("jo");
		if (modelStates.isConnected()){ chatNI.connect(modelUsername.getUsername(), false); }
		
		//chatController.performDisconnect();
		if (!modelStates.isConnected()){ chatNI.disconnect(modelUsername.getUsername()); }

		//chatGui.getwConnect();
		/*chatController.performDisconnect();*/
		//if (modelStates.isConnected()){)
		//chatController.performConnect("lilou");
		//remote.add((String)(ChatSystem.modelListUsers.getListUsers().keySet().iterator().next()));
		
		//chatController.performSendText("premier message",remote);
		//System.out.println(ChatSystem.getModelUsername().getUsername());
		/*chatController.performDisconnect();
		if (modelStates.isConnected()){
			System.out.println(modelUsername.getUsername() + " : disconnection succeed");
		}else{
			System.out.println(modelUsername.getUsername() + " : disconnection failed");
		}*/
	}

}