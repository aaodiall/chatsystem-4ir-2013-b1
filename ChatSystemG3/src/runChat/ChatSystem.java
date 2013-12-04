package runChat;

import java.net.UnknownHostException;
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
	private static ModelFile modelFile;
	private static ModelGroupRecipient modelGroupRecipient;

	
	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException{	
		// pour des tests locaux demander a l'utilisateur d'entrer un numero de port
		int portUDP=16001;
		int bufferSize = 50;
		//int maxFileTransfert = 5;
		modelListUsers = new ModelListUsers();
		modelUsername = new ModelUsername();
		modelStates = new ModelStates();
		modelGroupRecipient = new ModelGroupRecipient();
		modelText = new ModelText();
		//modelFile=new ModelFile();
		chatController = new Controller(modelListUsers,modelStates , modelText, modelUsername, modelGroupRecipient);
		chatNI = new ChatNI(portUDP,bufferSize,chatController);
		chatController.setChatNI(chatNI);
		chatGUI=new ChatGUI(chatController);
		chatController.setChatgui(chatGUI);
		modelUsername.addObserver(chatNI);
		modelStates.addObserver(chatNI);
		modelGroupRecipient.addObserver(chatNI);
		modelListUsers.addObserver(chatGUI);
		modelUsername.addObserver(chatGUI);
		modelStates.addObserver(chatGUI);
		modelText.addObserver(chatGUI);
		//modelFile.addObserver(chatGUI);
		
		/*
		 				/\
		  			   /  \				Je n'ai pas implémenter la relation de mon interface commandeLine
		 			  /	|  \			avec le ChatGui et le ChatGui ne tient pas encore compte
		             /  |	\			de CommandLine dans son update, on peut peut-être utiliser
		            /	|	 \			un attribut Mode dans ChatGUI et écrire deux fonctions de traitement. 
		           /	|	  \ 		Comme ça dans le update on a juste si mode == .. ->f1 sinon f2
		          /   	.	   \		 
		         /______________\    	
	*/
	}	
}