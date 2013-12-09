package runChat;

import chatSystemModel.*;
import chatSystemNetwork.*;
import chatSystemController.Controller;
import chatSystemIHMs.*;

/**
 * This class contains the main program to launch the chat system
 * @author joanna
 *
 */
public class ChatSystem {
	
	private static Controller chatController;
	private static ChatGUI chatGUI;
	private static ChatNI chatNI;
	private static ModelListUsers modelListUsers;
	private static ModelUsername modelUsername;
	private static ModelStates modelStates;
	private static ModelText modelText;

	
	/**
	 * main program : creates the essential objects and make the different associations
	 * @param args
	 * 
	 */
	public static void main(String[] args){	
		// pour des tests locaux demander a l'utilisateur d'entrer un numero de port
		int portUDP=16001;
		int bufferSize = 50;
		modelListUsers = new ModelListUsers();
		modelUsername = new ModelUsername();
		modelStates = new ModelStates();
		modelText = new ModelText();
		chatController = new Controller(modelListUsers,modelStates , modelText, modelUsername);
		chatNI = new ChatNI(portUDP,bufferSize,chatController);
		chatController.setChatNI(chatNI);
		chatGUI=new ChatGUI(chatController);
		chatController.setChatgui(chatGUI);
		modelUsername.addObserver(chatNI);
		modelStates.addObserver(chatNI);
		modelListUsers.addObserver(chatGUI);
		modelUsername.addObserver(chatGUI);
		modelStates.addObserver(chatGUI);
		modelText.addObserver(chatGUI);
	}	
}