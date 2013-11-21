package Controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import Interface.GuiToController;
import Model.ChatModel;
import Model.MessageFactory;
import Model.User;
import View.ChatGUI;
import View.ReceivedMessageNI;
import View.SendMessageNI;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;

import com.sun.istack.internal.logging.Logger;

public class ChatController implements GuiToController{
	private ChatModel chatModel;
	private ChatGUI chatGui;
	private FileTransfertController fileTransfertController;
	
	public ChatController(ChatModel chatModel,ChatGUI chatGui) {
		fileTransfertController = new FileTransfertController(this);
		this.chatModel = chatModel;
		this.chatGui = chatGui;
		Thread th = new Thread(ReceivedMessageNI.getInstance(this));
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
		
		Logger.getLogger(ChatController.class).log(Level.INFO, "Message received >> " + msg.toString());
		
		if(msg instanceof Hello) {
			//System.out.println("Hello");
			chatModel.addUser(user);
			if(!((Hello) msg).isAck())
				SendMessageNI.getInstance().sendMessage(MessageFactory.getHelloMessage(chatModel.getLocalUser().getUsername(), true), inetAddress);
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
		else
			fileTransfertController.receivedMessage(user, msg);
			
		return 1;
	}
	
	public void sendHelloMessage(User user) {
		SendMessageNI.getInstance().sendBroadcastMessage(MessageFactory.getHelloMessage(chatModel.getLocalUser().getUsername(), false));
	}
	
	public void sendGoodbyeMessage(User localUser) {
		SendMessageNI.getInstance().sendBroadcastMessage(MessageFactory.getByeMessage(chatModel.getLocalUser().getUsername()));
	}
	
	public void sendTextMessage(User user, String message) {
		Message text = new Text(chatModel.getLocalUser().getUsername(),message);
		chatModel.get(user).addMessage(text);
		SendMessageNI.getInstance().sendMessage(text,user.getAddress());
		chatGui.setChatText(getTalk(chatModel.indexOf(user)));
	}
	
	public void sendFile(User user, File selectedFile) {
		fileTransfertController.beginFileTransfertProtocol(user, selectedFile);
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
