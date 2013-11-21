package View;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;

import Controller.FileTransfertController;
import Controller.StateTransfert;
import Model.User;
import chatSystemCommon.FilePart;

import com.sun.istack.internal.logging.Logger;

public final class SendFileNI extends Thread{
	private static SendFileNI instance = null;

	private Socket socket;
	private User remoteUser;

	public StateTransfert getFileTransfertState() {
		return fileTransfertState;
	}

	public void setFileTransfertState(StateTransfert fileTransfertState) {
		this.fileTransfertState = fileTransfertState;
	}

	private StateTransfert fileTransfertState = StateTransfert.WAITING_INIT;
	private FileTransfertController fileTransfertController;

	private SendFileNI(FileTransfertController fileTransfertController) {
		this.fileTransfertController = fileTransfertController;
	}

	public final static SendFileNI getInstance(FileTransfertController fileTransfertController) {
		if(SendFileNI.instance == null) {
			synchronized(SendFileNI.class) {
				if(SendFileNI.instance == null)
					SendFileNI.instance = new SendFileNI(fileTransfertController);
			}
		}
		return SendFileNI.instance;
	}


	synchronized public void sendFile(User user){
		Logger.getLogger(SendFileNI.class).log(Level.INFO,"SendFile method called >> "+ user.toString());
		this.remoteUser = user;
		this.start();
	}

	public void run(){
		OutputStream os = null;
		while(this.fileTransfertState == StateTransfert.PROCESSING) {
			try {
				socket = new Socket(remoteUser.getAddress(), 16001);
				os = socket.getOutputStream();
				FilePart fp = fileTransfertController.getFilePartToSend();
				if(fp.isLast())
					this.fileTransfertState = StateTransfert.TERMINATED;
				os.write(fileTransfertController.getFilePartToSend().toArray());
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
