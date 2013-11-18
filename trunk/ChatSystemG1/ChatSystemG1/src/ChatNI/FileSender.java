package ChatNI;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import chatSystemCommon.FilePart;

public class FileSender implements Runnable {
	private int port;
	
	private ServerSocket servSock;
	private Socket clientSocket;
	public FileSender(int portEnvoi) throws IOException {
		// TODO Auto-generated constructor stub
		this.port = portEnvoi;
		this.servSock = new ServerSocket(port);
		System.out.println("on est la!!");
		clientSocket = servSock.accept();
		Thread a = new Thread(this, "sENDER tHREAD");
		a.start();
				
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
			

			
			while(!servSock.isClosed()){
				System.out.println("on est la!!");
			}
			
		
		
	}
	
	public void SendFilePart(FilePart fp) throws IOException{
		
        
        DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
		outToClient.write(fp.toArray());
		if(fp.isLast()){
			servSock.close();
		}
		 
	}

}
