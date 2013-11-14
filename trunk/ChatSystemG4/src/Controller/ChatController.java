package Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Message;
import chatSystemCommon.Text;
import Model.ChatModel;
import Model.MessageFactory;
import Model.User;
import View.ChatGUI;
import View.ReceivedNI;
import View.SendNI;

public class ChatController {
	private ChatModel chatModel;
	private ChatGUI chatGui;
	
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
			System.out.println("FileTransfertDemand");
		}	
		
		return 1;
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
