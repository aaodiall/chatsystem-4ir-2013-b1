package View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import chatSystemCommon.FilePart;

public final class SendFileNI extends Thread{
	private static SendFileNI instance = null;
	
	//private DatagramSocket datagramSocket;
	private Socket socket;
	private FilePart fileToSend;
	
	private SendFileNI() {
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
			fileToSend = file;
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
			oos.writeObject(fileToSend);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
