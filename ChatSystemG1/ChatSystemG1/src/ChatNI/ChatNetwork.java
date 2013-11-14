package ChatNI;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import Controller.ChatController;

import chatSystemCommon.*;

public class ChatNetwork implements Runnable{
	
	private ArrayList<FileReceiver> FileReceiverList;
	private ArrayList<FileSender> FileSenderList;
	private Receiver MessageReceiver;
	private Sender MessageSender;
	
	public ChatNetwork() {
		// TODO Auto-generated constructor stub
		setMessageReceiver(new Receiver());
		MessageSender = new Sender();
	}
	
	
	
	public static void NotifyMessageReceive(Message message) throws UnknownHostException{
		if(!message.getUsername().equals(ChatController.getLocalUsername()+"@"+InetAddress.getLocalHost().getHostAddress().toString()) ){
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
				ChatController.FileCancelProcessing( message);
			}else if(message.getClass() == FileTransfertConfirmation.class){
				ChatController.FileAcceptanceProcessing((FileTransfertConfirmation)message);
			}
		}
	}
	
	public void SendBye(){
		
		Goodbye b = new Goodbye(ChatController.getLocalUsername());
		System.out.println("on broadcast un bye");

		MessageSender.BroadCastMessage(b);
		
	}
	
	public void SendFile(){
		
	}
	
	public void SendFileTransfertCancel(FileTransfertCancel ftca,String remoteUsername){
		System.out.println("on envoie un ftca");
		MessageSender.SendMessage(ftca,ChatController.extractIpFromUserName(remoteUsername));
	}
	public void SendFileTransfertConfirmation(FileTransfertConfirmation ftco,String remoteUsername){
		System.out.println("on envoie un ftco");
		MessageSender.SendMessage(ftco,ChatController.extractIpFromUserName(remoteUsername));
	}
	public void SendFileAcceptance(FileTransfertDemand ftd, String remoteUsername){
		System.out.println("on envoie un ftd");
		MessageSender.SendMessage(ftd,ChatController.extractIpFromUserName(remoteUsername));
	}
	
	public void SendHello(boolean isAck, String RemoteUsername){
		
		Hello h = new Hello(ChatController.getLocalUsername(),isAck);
		
		if(isAck){
			System.out.println("on envoie un ack");
			MessageSender.SendMessage(h,ChatController.extractIpFromUserName(RemoteUsername));
		}else{
			System.out.println("on broadcast un hello");
			MessageSender.BroadCastMessage(h);
		}
		
	}
	
	/**
	 * @return the messageSender
	 */
	public Sender getMessageSender() {
		return MessageSender;
	}



	/**
	 * @param messageSender the messageSender to set
	 */
	public void setMessageSender(Sender messageSender) {
		MessageSender = messageSender;
	}



	public void SendMessage(Message m, String[] UsernameList){
		if(UsernameList.length == 1){
			MessageSender.SendMessage(m,ChatController.extractIpFromUserName(UsernameList[0]));
		}else{
			
			String[] UsernameIpList = new String[UsernameList.length];
			for(int i = 0;i<UsernameList.length;i++){
				
				UsernameIpList[i]=ChatController.extractIpFromUserName(UsernameList[i]);
				
			}
			MessageSender.MultiCastMessage(m,UsernameIpList);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}



	/**
	 * @return the messageReceiver
	 */
	public Receiver getMessageReceiver() {
		return MessageReceiver;
	}



	/**
	 * @param messageReceiver the messageReceiver to set
	 */
	public void setMessageReceiver(Receiver messageReceiver) {
		MessageReceiver = messageReceiver;
	}
	
	public static InetAddress BroadcastAddress() {
        boolean Addressfound = false;
        InetAddress broadcast = null;
        try {
            Enumeration<NetworkInterface> interfaces =NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements() && !Addressfound)  {
                NetworkInterface ni = interfaces.nextElement();
                if (!ni.isLoopback()) {
                	List<InterfaceAddress> addresses = ni.getInterfaceAddresses();
                	for(InterfaceAddress ia : addresses){
                		broadcast = ia.getBroadcast();
                        if (broadcast != null && !Addressfound )
                            Addressfound = true;
                    }
                }
                
            }
        } catch (SocketException exc) {
            System.out.println("no interface");
        }
       
        return broadcast;
    }



	/**
	 * @return the fileReceiverList
	 */
	public ArrayList<FileReceiver> getFileReceiverList() {
		return FileReceiverList;
	}



	/**
	 * @param fileReceiverList the fileReceiverList to set
	 */
	public void setFileReceiverList(ArrayList<FileReceiver> fileReceiverList) {
		FileReceiverList = fileReceiverList;
	}



	/**
	 * @return the fileSenderList
	 */
	public ArrayList<FileSender> getFileSenderList() {
		return FileSenderList;
	}



	/**
	 * @param fileSenderList the fileSenderList to set
	 */
	public void setFileSenderList(ArrayList<FileSender> fileSenderList) {
		FileSenderList = fileSenderList;
	}
}
