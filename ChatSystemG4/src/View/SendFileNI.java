package View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Controller.FileTransfertController;
import Controller.StateTransfert;
import Model.User;
import chatSystemCommon.FilePart;

public final class SendFileNI extends Thread{
	private static SendFileNI instance = null;
	
	//private DatagramSocket datagramSocket;
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
		//try {
			//datagramSocket = new DatagramSocket(16000);
			//datagramSocket.setBroadcast(true);
		//} catch (SocketException e) {
		//	e.printStackTrace();
		//}
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
			socket = new Socket(user.getAddress(), 16000);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		FileOutputStream fos;
        ObjectOutputStream oos;
		try {
			fos = (FileOutputStream) socket.getOutputStream();
			oos = new ObjectOutputStream(fos);
			oos.write(fileTransfertController.getFilePartToSend().getFilePart());
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
