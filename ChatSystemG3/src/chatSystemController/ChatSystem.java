package chatSystemController;
import chatSystemModel.*;
import chatSystemNetwork.*;
import chatSystemIHMs.*;

public class ChatSystem {

	
	private static Controller chatController;
	private static ChatGui chatGui;
	private static ChatNI chatNI;
	private static ModelListUsers modelListUsers;
	private static ModelUsername modelUsername;
	private static ModelStates modelStates;
	private static ModelText modelText;
	
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		chatController = new Controller();
		chatNI = new ChatNI(16002);
		modelListUsers = new ModelListUsers();
		modelUsername = new ModelUsername();
		modelStates = new ModelStates();
		chatController.performConnect("lilou");
		/*chatController.performDisconnect();
		if (modelStates.isConnected()){
			System.out.println(modelUsername.getUsername() + " : disconnection succeed");
		}else{
			System.out.println(modelUsername.getUsername() + " : disconnection failed");
		}*/
	}

}
