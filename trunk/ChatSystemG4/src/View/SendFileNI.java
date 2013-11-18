package View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Controller.StateTransfert;
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
	
	private SendFileNI() {
		//try {
			//datagramSocket = new DatagramSocket(16000);
			//datagramSocket.setBroadcast(true);
		//} catch (SocketException e) {
		//	e.printStackTrace();
		//}
	}
	
	public final static SendFileNI getInstance() {
		if(SendFileNI.instance == null) {
			synchronized(SendFileNI.class) {
				if(SendFileNI.instance == null)
					SendFileNI.instance = new SendFileNI();
			}
		}
		return SendFileNI.instance;
	}
	
	synchronized public void sendFile(FilePart file, InetAddress address){
		try {
			socket = new Socket(address, 16000);
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
			//oos.writeObject(fileToSend);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
