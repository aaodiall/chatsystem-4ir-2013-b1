import chatSystemModelController.*;
import chatSystemNetwork.*;
import chatSystemCommon.Message;
//import chatSystemCommand
import chatSystemIHMs.*;

public class ChatSystem {

	
	private static Controller chatController;
	//private static ChatGUI chatGUI;
	private static ChatNI chatNI;
	private static ModelListUsers modelListUsers;
	private static ModelUsername modelUsername;
	private static ModelStates modelStates;
	//private static ModelText modelText
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		chatController = new Controller();
		chatNI = new ChatNI();
		modelListUsers = new ModelListUsers();
		modelUsername = new ModelUsername();
		modelStates = new ModelStates();
		chatController.performConnect("lilou");
		if (modelStates.isConnected()){
			System.out.println(modelUsername.getUsername() + " : connection succeed");
		}else{
			System.out.println(modelUsername.getUsername() + " : connection failed");
		}
		/*chatController.performDisconnect();
		if (modelStates.isConnected()){
			System.out.println(modelUsername.getUsername() + " : disconnection succeed");
		}else{
			System.out.println(modelUsername.getUsername() + " : disconnection failed");
		}*/
	}

}
