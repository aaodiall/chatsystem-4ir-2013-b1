/**
 * 
 */
package chatSystemNetwork;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import chatSystemCommon.FilePart;
import chatSystemCommon.Message;

/**
 * @author joanna
 * le Receiver se connecte au port distant et reçoit le fichier
 */
public class ChatNIStreamReceiver extends Thread{

	private Socket rSocket;
	private int remotePort;
	private InetAddress remoteIP;
	private ObjectInputStream objectReader;
	private byte[] bytes;
	private boolean isReceived;
	private ArrayList<byte[]> fparts;
	private int numberOfParts;
	
	public ChatNIStreamReceiver(int remotePort,InetAddress remoteIP, int numberOfParts){
		this.remotePort = remotePort;
		this.remoteIP = remoteIP;
		this.isReceived = false;
		this.fparts = new ArrayList<byte[]>();
		this.numberOfParts = numberOfParts;
	}

	public boolean getIsReceived(){
		return this.isReceived;
	}
	
	public ArrayList<byte[]> getAllParts(){
		return this.fparts;
	}
	
	public int getPort(){
		return this.remotePort;
	}
	
	public void run(){
		int i=0;
		Object objectRead;
		FilePart f;
		try {
			this.rSocket = new Socket(this.remoteIP,this.remotePort);
			this.bytes = new byte[1204];//this.rSocket.getReceiveBufferSize()];
			this.objectReader = new ObjectInputStream(this.rSocket.getInputStream());
			while (isReceived == false){
				 objectRead= this.objectReader.readObject();
				 f = (FilePart)objectRead;
				 this.fparts.add(f.getFilePart());
				 if (f.isLast() == true){
					 this.isReceived = true;
				 }
			}
			System.out.println("file received");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		} finally{
			try {
				this.objectReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
