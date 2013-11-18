package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import Model.MessageFactory;
import Model.User;
import View.SendFileNI;
import View.SendMessageNI;

public class FileTransfertController {
	private ArrayList<byte[]> fileSequence = new ArrayList<byte[]>();
	
	
	public FileTransfertController() {
		
	}
	
	public void beginFileTransfertProtocol(User user, File file) {
		if(SendFileNI.getInstance().getFileTransfertState() == StateTransfert.WAITING_INIT) {
			this.splitFile(file);
		}
		else {
			System.out.println("Imposible d'envoyer un fichier, un transfert est déjà en cours");
		}
		
		this.sendFileTransfertDemand(user,file);
	}
	
	public void updateState() {
		switch(SendFileNI.getInstance().getFileTransfertState()) {
			case WAITING_INIT :
				SendFileNI.getInstance().setFileTransfertState(StateTransfert.AVAILABLE);
			break;
			case AVAILABLE:
				SendFileNI.getInstance().setFileTransfertState(StateTransfert.WAITING_CONFIRMATION);
			break;
			case WAITING_CONFIRMATION :
				SendFileNI.getInstance().setFileTransfertState(StateTransfert.READY);
			break;
			case READY :
				SendFileNI.getInstance().setFileTransfertState(StateTransfert.PROCESSING);
			break;
			case PROCESSING :
				SendFileNI.getInstance().setFileTransfertState(StateTransfert.TERMINATED);
			break;
			case TERMINATED :
				SendFileNI.getInstance().setFileTransfertState(StateTransfert.WAITING_INIT);
			break;
			case CANCELED :
				SendFileNI.getInstance().setFileTransfertState(StateTransfert.WAITING_INIT);
			break;
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
	
	public void sendFileTransfertDemand(User user,File file) {
		SendMessageNI.getInstance().sendMessage(MessageFactory.getFileTransfertDemandMessage(user.getUsername(), file.getName(), file.length(),16000), user.getAddress());
	}
}
