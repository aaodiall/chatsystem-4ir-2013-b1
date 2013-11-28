/**
 * 
 */
package chatSystemNetwork;

import java.io.BufferedInputStream;
import java.io.IOException;
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
	private BufferedInputStream reader;
	private int nextByte;
	private byte[] bytes;
	private boolean isReceived;
	private ArrayList<byte[]> fparts;
	private int numberOfParts;
	
	public ChatNIStreamReceiver(int remotePort,InetAddress remoteIP, int numberOfParts){
		this.remotePort = remotePort;
		this.remoteIP = remoteIP;
		this.nextByte = 0;
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
		int i =0;
		try {
			this.rSocket = new Socket(this.remoteIP,this.remotePort);
			this.bytes = new byte[1000000];
			this.reader = new BufferedInputStream(this.rSocket.getInputStream());
			while (isReceived == false){
				while (nextByte != -1){
					nextByte = this.reader.read();
					bytes[i] = (byte)nextByte;
					i++;
				}
				FilePart f = (FilePart)Message.fromArray(this.bytes);
				this.fparts.add(f.getFilePart());
				if (f.isLast() == true){
					this.isReceived = true;
				}
			}
			System.out.println("part received");
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
