package runChat;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

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
		// pour des tests locaux demander a l'utilisateur d'entrer un numero de port
		int portUDP=16001;
		int bufferSize = 50;
		Thread chatNIThread;
		modelListUsers = new ModelListUsers();
		modelUsername = new ModelUsername();
		modelStates = new ModelStates();
		modelGroupRecipient = new ModelGroupRecipient();
		modelText = new ModelText();
		chatController = new Controller(modelListUsers,modelStates , modelText, modelUsername, modelGroupRecipient);
		chatNI = new ChatNI(portUDP,bufferSize,chatController);
		chatController.setChatNI(chatNI);
		chatNIThread = new Thread(chatNI);
		chatNIThread.start();
		modelUsername.addObserver(chatNI);
		modelStates.addObserver(chatNI);
		modelGroupRecipient.addObserver(chatNI);
		// TEST du System avec GUI -> modifier aussi le ChatController
		/*chatGUI=new ChatGUI(chatController);
		chatController.setChatgui(chatGUI);
		modelListUsers.addObserver(chatGUI);*/
		
		// TEST de CONNEXION sans GUI -> modifier aussi le ChatController
		String pseudo;
		String text = "";
		Scanner sc = new Scanner(System.in);
		System.out.println("entre un pseudo : ");
		pseudo = sc.nextLine();
		chatController.performConnect(pseudo);
		chatController.connectReceived("alpha",InetAddress.getByName("alpha"), false);
		chatController.performAddURecipient("alpha");
		System.out.println("entre un message : ");
		text =sc.nextLine();
		chatController.performSendText(text);
		chatController.messageReceived(text, "alpha");
		chatController.performDisconnect();
		//if (modelStates.isConnected()){ chatNI.connect(modelUsername.getUsername(), false); }
		//while(modelListUsers.getListUsers().keySet().iterator().hasNext()){//remote.add((String)(ChatSystem.modelListUsers.getListUsers().keySet().iterator().next()));
			//System.out.println("user : " + modelListUsers.getListUsers().keySet().iterator().next());
		//}
		//chatController.performSendText("premier message",remote);	
		//
		//if (!modelStates.isConnected()){ chatNI.disconnect(modelUsername.getUsername()); }

		//chatGui.getwConnect();
		/*chatController.performDisconnect();*/
		//if (modelStates.isConnected()){)
		//chatController.performConnect("lilou");		
	}

}