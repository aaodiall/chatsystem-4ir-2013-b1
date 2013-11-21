package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.sun.istack.internal.logging.Logger;

import chatSystemCommon.FilePart;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Message;
import Model.MessageFactory;
import Model.User;
import View.ReceivedFileNI;
import View.SendFileNI;
import View.SendMessageNI;

public class FileTransfertController {
	private ArrayList<byte[]> fileSequence = new ArrayList<byte[]>();
	private ChatController chatController;
	
	/**
	 * 
	 * @param chatController
	 */
	public FileTransfertController(ChatController chatController) {
		this.chatController = chatController;
	}
	
	/**
	 * 
	 * @param user
	 * @param file
	 */
	public void beginFileTransfertProtocol(User user, File file) {
		if(SendFileNI.getInstance(this).getFileTransfertState() == StateTransfert.WAITING_INIT) {
			this.splitFile(file);
		}
		else
			Logger.getLogger(ChatController.class).log(Level.INFO, "Imposible d'envoyer un fichier, un transfert est déjà en cours");
		
		this.sendFileTransfertDemand(user,file);	
	}
	
	private void sendFile(User user) {
		SendFileNI.getInstance(this).sendFile(user);
	}

	/**
	 * 
	 */
	public void updateState() {
		switch(SendFileNI.getInstance(this).getFileTransfertState()) {
			case WAITING_INIT :	
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.AVAILABLE);
			break;
			case AVAILABLE:
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.WAITING_CONFIRMATION);
			break;
			case WAITING_CONFIRMATION :
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.READY);
			break;
			case READY :
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.PROCESSING);
			break;
			case PROCESSING :
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.TERMINATED);
			break;
			case TERMINATED :
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.WAITING_INIT);
			break;
			case CANCELED :
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.WAITING_INIT);
			break;
		}
	}
	
	/**
	 * 
	 * @param user
	 * @param msg
	 */
	public void receivedMessage(User user, Message msg) {
		if(msg instanceof FileTransfertCancel) {
			//System.out.println("FileTransfertCancel");
		}
		else if(msg instanceof FileTransfertConfirmation) {
			//System.out.println("FileTransfertConfirmation");
			if(((FileTransfertConfirmation) msg).isAccepted()) {	
				this.sendFile(user);
			}
		}
		else if(msg instanceof FileTransfertDemand) {
			int option = JOptionPane.showConfirmDialog(null, "Vous avez reçu une demande de transfert de fichier de la part de "+msg.getUsername()+"\n Nom du fichier : "+ ((FileTransfertDemand) msg).getName()+"\nTaille (en byte) : "+((FileTransfertDemand) msg).getSize()+"\n\nVoulez-vous accepter le fichier ?", "Demande de transfert de fichier reçue", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.OK_OPTION) {
				ReceivedFileNI.getInstance(chatController,((FileTransfertDemand) msg).getPortClient()).start();
				this.sendFileTransfertConfirmation(user, true, msg.getId());
			}
			else
				this.sendFileTransfertConfirmation(user, false, msg.getId());
		}
	}
	
	/**
	 * 
	 * @param file
	 */
	public void splitFile(File file) {
		byte[] bFile = new byte[(int) file.length()];
	    FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
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
				fileSequence.add(Arrays.copyOfRange(bFile,i*1000,bFile.length));
			else
				fileSequence.add(Arrays.copyOfRange(bFile,i*1000,i*1000+1000));
		}
		
		this.updateState();
	}
	
	/**
	 * 
	 * @param user
	 * @param file
	 */
	public void sendFileTransfertDemand(User user,File file) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertDemandMessage(chatController.getLocalUser().getUsername(), file.getName(), file.length(),16001), user.getAddress());
	}

	/**
	 * 
	 * @param user
	 * @param isAccepetd
	 * @param idDemand
	 */
	public void sendFileTransfertConfirmation(User user, boolean isAccepetd, int idDemand) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertConfirmationMessage(chatController.getLocalUser().getUsername(), isAccepetd, idDemand), user.getAddress());
	}

	public FilePart getFilePartToSend() {
		byte[] toSend = null;
		if(fileSequence.size() > 0) {
			toSend = fileSequence.get(0);
			fileSequence.remove(0);
			return MessageFactory.getFileMessage(chatController.getLocalUser().getUsername(), toSend, false);
		}
		return MessageFactory.getFileMessage(chatController.getLocalUser().getUsername(), toSend, true);
	}
}
