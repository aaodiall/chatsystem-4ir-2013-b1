package View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;

import com.sun.istack.internal.logging.Logger;

import Controller.FileTransfertController;
import Controller.StateTransfert;
import Model.User;
import chatSystemCommon.FilePart;
import chatSystemCommon.Message;

public final class SendFileNI extends Thread{
	private static SendFileNI instance = null;
	
	private Socket socket;
	
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
		try {
			Logger.getLogger(SendFileNI.class).log(Level.INFO,"SendFile method called >> "+ user.toString());
			socket = new Socket(user.getAddress(), 16001);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
        OutputStream oo;
		try {
			oo = socket.getOutputStream();
			oo.write(fileTransfertController.getFilePartToSend().toArray());
			oo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
