package ChatNI;

import java.util.ArrayList;

import Controller.ChatController;

import chatSystemCommon.*;

public class ChatNetwork implements Runnable{
	
	private ArrayList<FileReceiver> FileReceiverList;
	private ArrayList<FileSender> FileSenderList;
	private Receiver MessageReceiver;
	private Sender MessageSender;
	
	public ChatNetwork() {
		// TODO Auto-generated constructor stub
		
	}
	
	
	
	public static void NotifyMessageReceive(Message message){
		if(message.getClass() == Hello.class){
			ChatController.HelloProcessing((Hello) message);
		}else if(message.getClass() == Goodbye.class){
			ChatController.ByeProcessing((Goodbye) message);
		}else if(message.getClass() == Text.class){
			ChatController.MessageProcessing((Text) message);
		}else if(message.getClass() == File.class){
			ChatController.FileProcessing((File) message);
		}else if(message.getClass() == FileTransfertDemand.class){
			ChatController.FileTransfertDemandProcessing((FileTransfertDemand) message);
		}else if(message.getClass() == FileTransfertCancel.class){
			ChatController.FileAcceptanceProcessing( message);
		}else if(message.getClass() == FileTransfertConfirmation.class){
			ChatController.FileAcceptanceProcessing(message);
		}
	}
	
	public void SendBye(){
		
		Goodbye b = new Goodbye(ChatController.getLocalUsername());
		MessageSender.BroadCastMessage(b);
		
	}
	
	public void SendFile(){
		
	}
	
	public void SendFileAcceptance(){
		
	}
	
	public void SendHello(boolean isAck, String RemoteUsername){
		
		Hello h = new Hello(ChatController.getLocalUsername(),isAck);
		
		if(isAck){
			MessageSender.SendMessage(h,ChatController.extractIpFromUserName(RemoteUsername));
		}else{
			MessageSender.BroadCastMessage(h);
		}
		
	}
	
	public void SendMessage(){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
