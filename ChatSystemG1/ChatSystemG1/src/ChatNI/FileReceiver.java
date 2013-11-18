package ChatNI;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import chatSystemCommon.FilePart;
import chatSystemCommon.Message;

public class FileReceiver implements Runnable{
	private int compteurRecept = 0;
	private int port;
	private String ip;
	private Socket clientSocket;
	public FileReceiver(String remoteUser, int portClient) {
		// TODO Auto-generated constructor stub
		this.port = portClient;
		this.ip = remoteUser;
		Thread t = new Thread(this, "fILEReceiver Thread");
		t.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			  
			this.clientSocket = new Socket(ip,port);
			while(!clientSocket.isClosed()){
				 

				 
				byte[] mes = new byte[ 1225 ];  
				DataInputStream is = new DataInputStream(clientSocket.getInputStream());
				try{
				is.readFully(mes);
				ReceiveFilePart(mes);
				}catch(EOFException e){
					clientSocket.close();
				}
				
			}
			System.out.println("on ferme ce thread receiver");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ReceiveFilePart(byte[] mes)  {
		Message m = null;
		
			
			try {
				m =  Message.fromArray(mes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("on a recu " + m.toString() );
			System.out.println("on a recu "  );
			m.setUsername(m.getUsername() + "@" + ip);
			if(((FilePart) m).isLast()){
				System.out.println("on ferme ce thread receiver");
				try {
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				ChatNetwork.NotifyMessageReceive(m);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
	}

}
