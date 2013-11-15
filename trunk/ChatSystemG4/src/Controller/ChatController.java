package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import Interface.GuiToController;
import Model.ChatModel;
import Model.MessageFactory;
import Model.User;
import View.ChatGUI;
import View.ReceivedFileNI;
import View.ReceivedNI;
import View.SendFileNI;
import View.SendNI;
import chatSystemCommon.FilePart;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;

public class ChatController implements GuiToController{
	private ChatModel chatModel;
	private ChatGUI chatGui;
	private ArrayList<FilePart> fileParts;
	private String path;
	
	public ChatController(ChatModel chatModel,ChatGUI chatGui) {
		this.chatModel = chatModel;
		this.chatGui = chatGui;
		Thread th = new Thread(ReceivedNI.getInstance(this));
		th.start();
	}

	public int receivedMessage(InetAddress inetAddress, Message msg) {
		try {
			if(inetAddress.equals(InetAddress.getLocalHost()))
				return -1;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		User user = new User(inetAddress,msg.getUsername());
		
		if(msg instanceof Hello) {
			//System.out.println("Hello");
			chatModel.addUser(user);
			if(!((Hello) msg).isAck())
				SendNI.getInstance().sendMessage(MessageFactory.getHelloMessage(chatModel.getLocalUser().getUsername(), true), inetAddress);
		}
		else if(msg instanceof Goodbye) {
			//System.out.println("Goodbye");
			chatModel.removeElement(new User(inetAddress,msg.getUsername()));
		}
		else if(msg instanceof Text) {
			//System.out.println("Text");
			int selectedIndex = chatGui.getUserList().getSelectedIndex();		
			chatModel.get(user).addMessage(msg);
			if(selectedIndex != -1)
				if(chatModel.get(selectedIndex).equals(user))
					chatGui.setChatText(getTalk(selectedIndex));
		}
		else if(msg instanceof FileTransfertCancel) {
			System.out.println("FileTransfertCancel");
		}
		else if(msg instanceof FileTransfertConfirmation) {
			System.out.println("FileTransfertConfirmation");
		}
		else if(msg instanceof FileTransfertDemand) {
			//System.out.println("FileTransfertDemand");
			//show option pane
			int value = chatGui.showConfirmationPane(msg.getUsername());
			if(value == 1 || value == -1){ //Reject fileTransfer pressing no or closing the window
			//send demand rejected msg
				Message cancel = new FileTransfertCancel(chatModel.getLocalUser().getUsername(), msg.getId());
				chatModel.get(user).addMessage(cancel);
				SendNI.getInstance().sendMessage(cancel,user.getAddress());
				chatGui.setChatText(getTalk(chatModel.indexOf(user)));
			}else if(value == 1){// Accept the file transfer demand pressing yes
			//set the path where the file will be saved
				path = chatGui.getSavingPath(); // We cant accept 2 files cause their saving paths would be overwritten
			//Start ReceivedFileNI to get the file parts
				Runnable receivedFile = ReceivedFileNI.getInstance(this);
				new Thread (receivedFile).start();
			//send confirmation msg
				Message confirm = new FileTransfertConfirmation(chatModel.getLocalUser().getUsername(), true, msg.getId()); //Is this "true" always true?
				chatModel.get(user).addMessage(confirm);
				SendNI.getInstance().sendMessage(confirm, user.getAddress());
				chatGui.setChatText(getTalk(chatModel.indexOf(user)));
			}
		}
		else if(msg instanceof FilePart){
			fileParts.add((FilePart)msg);
			if(((FilePart) msg).isLast())
				saveFile();			
		}
		
		return 1;
	}
	
	private void saveFile() {
		//Get length of the parts
		int length = 0;
		for(int i=0; i<fileParts.size();i++)
			length+= fileParts.get(i).getFilePart().length;
		//Ensemble the parts
		byte[] partsEnsambled = new byte[length];
		for(int i=0; i<fileParts.size();i++)
			partsEnsambled = fileParts.get(i).getFilePart();
		//Save the file
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(partsEnsambled);
			fos.close();
		}
		catch(FileNotFoundException ex)   {
			System.out.println("FileNotFoundException : " + ex);
		}
		catch(IOException ioe)  {
			System.out.println("IOException : " + ioe);
		}
	}

	public void sendFile(User user, File selectedFile) {
		//convert file into an array of bytes
		byte[] bFile = new byte[(int) selectedFile.length()];
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(selectedFile);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Divide the file in parts and send them
		int fileParts = (bFile.length /1000)+1; //Correct: calculate the parts correctly
		for(int i=0; i<fileParts; i++){
			if(i+1==fileParts) //Sends last part
				SendFileNI.getInstance().sendFile( 
						new FilePart(chatModel.getLocalUser().getUsername(), Arrays.copyOfRange(bFile,i*1000,
								bFile.length), true), user.getAddress());
			else
				SendFileNI.getInstance().sendFile( 
						new FilePart(chatModel.getLocalUser().getUsername(), Arrays.copyOfRange(bFile,i*1000,
								i*1000+1000), false), user.getAddress());
		}
	}
	
	public void sendHelloMessage(User user) {
		SendNI.getInstance().sendBroadcastMessage(MessageFactory.getHelloMessage(chatModel.getLocalUser().getUsername(), false));
	}
	
	public void sendGoodbyeMessage(User localUser) {
		SendNI.getInstance().sendBroadcastMessage(MessageFactory.getByeMessage(chatModel.getLocalUser().getUsername()));
	}
	
	public void sendTextMessage(User user, String message) {
		Message text = new Text(chatModel.getLocalUser().getUsername(),message);
		chatModel.get(user).addMessage(text);
		SendNI.getInstance().sendMessage(text,user.getAddress());
		chatGui.setChatText(getTalk(chatModel.indexOf(user)));
	}
	
	public String getTalk(int index) {
		if(index==-1)
			return "";
		
		User u = chatModel.get(index);
		String talk = "";
		
		for(Message m : u.getMessageList()) {
			if(m instanceof Text)
				talk += m.getUsername() + " : " + ((Text) m).getText() + "\n";
		}
		return talk;
	}
	
	public String getCurrentTime() {
		Date now = new Date();
		String currentTime = new SimpleDateFormat("HH:mm:ss").format(now);
		return currentTime;
	}
	
	public ChatModel getChatModel() {
		return chatModel;
	}

	public void setChatModel(ChatModel chatModel) {
		this.chatModel = chatModel;
	}
	
	public User getLocalUser() {
		return chatModel.getLocalUser();
	}
}
