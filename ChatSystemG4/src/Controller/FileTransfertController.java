package Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Model.MessageFactory;
import Model.User;
import View.ReceivedFileNI;
import View.SendFileNI;
import View.SendMessageNI;
import chatSystemCommon.FilePart;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Message;

import com.sun.istack.internal.logging.Logger;

public class FileTransfertController {
	private ArrayList<byte[]> sendBuffer = new ArrayList<byte[]>();
	//private ArrayList<byte[]> receivedBuffer = new ArrayList<byte[]>();
	private ByteBuffer receivedBuffer;
	private byte[] receivedFile;
	private ChatController chatController;
	private String fileName;
	private long fileSize;

	public FileTransfertController(ChatController chatController) {
		this.chatController = chatController;
	}

	public void beginFileTransfertProtocol(User user, File file) {
		if(SendFileNI.getInstance(this).getFileTransfertState() == StateTransfert.WAITING_INIT) {
			this.fileTransfertProtocol(user, file,null);
		}
		else
			Logger.getLogger(ChatController.class).log(Level.INFO, "Imposible d'envoyer un fichier, un transfert est déjà en cours");
	}

	public void fileTransfertProtocol(User user, File file, Message msg) {
		Logger.getLogger(FileTransfertController.class).log(Level.INFO, SendFileNI.getInstance(this).getFileTransfertState().name());
		switch(SendFileNI.getInstance(this).getFileTransfertState()) {
		case WAITING_INIT :	
			this.splitFile(file);
			SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.AVAILABLE);
			this.fileTransfertProtocol(user, file, msg);
			break;
		case AVAILABLE:
			this.sendFileTransfertDemand(user,file);	
			SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.WAITING_CONFIRMATION);
			this.fileTransfertProtocol(user, file, msg);
			break;
		case WAITING_CONFIRMATION :
			SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.READY);
			//this.fileTransfertProtocol(user, file, msg);
			break;
		case READY :
			if(((FileTransfertConfirmation) msg).isAccepted()) {
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.PROCESSING);
				this.sendFile(user);
			}
			else {
				SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.CANCELED);
			}
			this.fileTransfertProtocol(user, file, msg);
			break;
		case PROCESSING :
			SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.TERMINATED);
			//this.fileTransfertProtocol(user, file, msg);
			break;
		case TERMINATED :
			SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.WAITING_INIT);
			break;
		case CANCELED :
			receivedBuffer.clear();
			sendBuffer.clear();
			SendFileNI.getInstance(this).setFileTransfertState(StateTransfert.WAITING_INIT);
			break;
		}
	}

	public void receivedMessage(User user, Message msg) {
		if(msg instanceof FileTransfertCancel) {

		}
		else if(msg instanceof FileTransfertConfirmation) {
			this.fileTransfertProtocol(user, null, msg);
		}
		else if(msg instanceof FileTransfertDemand) {
			this.fileName = ((FileTransfertDemand) msg).getName();
			this.fileSize = ((FileTransfertDemand) msg).getSize();
			this.receivedFile = new byte[(int) this.fileSize];
			this.receivedBuffer = ByteBuffer.wrap(receivedFile);
			int option = JOptionPane.showConfirmDialog(null, "Vous avez reçu une demande de transfert de fichier de la part de "+msg.getUsername()+"\n Nom du fichier : "+ this.fileName +"\nTaille (en byte) : "+ this.fileSize +"\n\nVoulez-vous accepter le fichier ?", "Demande de transfert de fichier reçue", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.OK_OPTION) {
				ReceivedFileNI.getInstance(chatController,((FileTransfertDemand) msg).getPortClient()).start();
				this.sendFileTransfertConfirmation(user, true, msg.getId());
			}
			else
				this.sendFileTransfertConfirmation(user, false, msg.getId());
		}
		else if(msg instanceof FilePart) {
			receivedBuffer.put(((FilePart) msg).getFilePart());
			
			if(((FilePart) msg).isLast()) {
				JFileChooser fileChooser = new JFileChooser(); 
				fileChooser.setDialogTitle("Select directory");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				int value = fileChooser.showOpenDialog(chatController.getChatGui());
				while(value != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(chatController.getChatGui(), "You must select a directory.");
					value = fileChooser.showOpenDialog(chatController.getChatGui());
				}
				
				try {
					FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile().toString()+"\\"+this.fileName);
					fos.write(this.receivedFile);
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

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
		//Divide the file in parts
		int fileParts = (bFile.length /2000)+1; //Correct: calculate the parts correctly
		for(int i=0; i<fileParts; i++){
			if(i+1==fileParts) //Sends last part
				sendBuffer.add(Arrays.copyOfRange(bFile,i*2000,bFile.length));
			else
				sendBuffer.add(Arrays.copyOfRange(bFile,i*2000,i*2000+2000));
		}
	}

	public void sendFileTransfertDemand(User user,File file) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertDemandMessage(chatController.getLocalUser().getUsername(), file.getName(), file.length(),16001), user.getAddress());
	}

	public void sendFileTransfertConfirmation(User user, boolean isAccepetd, int idDemand) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertConfirmationMessage(chatController.getLocalUser().getUsername(), isAccepetd, idDemand), user.getAddress());
	}

	private void sendFile(User user) {
		SendFileNI.getInstance(this).sendFile(user);
	}

	public FilePart getFilePartToSend() {
		byte[] toSend = null;
		if(sendBuffer.size() >0) {
			toSend = sendBuffer.get(0);
			sendBuffer.remove(0);
			if(sendBuffer.size() != 1) 
				return MessageFactory.getFileMessage(chatController.getLocalUser().getUsername(), toSend, false);
			else
				return MessageFactory.getFileMessage(chatController.getLocalUser().getUsername(), toSend, true);
		}
		return null;
	}
}
