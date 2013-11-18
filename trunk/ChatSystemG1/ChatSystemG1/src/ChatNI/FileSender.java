package ChatNI;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import chatSystemCommon.FilePart;

public class FileSender implements Runnable {
	private int port;
	private int compteurpart = 0;
	private ServerSocket servSock;
	private Socket clientSocket;
	public FileSender(int portEnvoi) throws IOException {
		// TODO Auto-generated constructor stub
		this.port = portEnvoi;
		this.servSock = new ServerSocket(port);
		clientSocket = servSock.accept();
		Thread a = new Thread(this, "SENDER THREAD");
		a.start();
				
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
			

			
			while(!servSock.isClosed()){
				
			}
			System.out.println("on ferme ce thread");
		
		
	}
	
	public void SendFilePart(FilePart fp) throws IOException{
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(clientSocket.getPort());
       
        byteStream.flush();
        byteStream.write(fp.toArray());
        System.out.println("taille du fp : " + fp.toArray().length);
       
        byteStream.flush();
		
		
        //DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
		//outToClient.write(fp.toArray());
		
		System.out.println("on envoie la part numero : " + compteurpart++);
		if(fp.isLast()){
			clientSocket.close();
			servSock.close();
		}
		 
	}

}
