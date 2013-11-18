package ChatNI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import chatSystemCommon.FilePart;

public class FileSender implements Runnable {
	private int port;
	
	private ServerSocket servSock;
	private Socket clientSocket;
	public FileSender(int portEnvoi) {
		// TODO Auto-generated constructor stub
		this.port = portEnvoi;
		
		Thread t = new Thread();
		t.start();
				
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.servSock = new ServerSocket(port);
			 clientSocket = servSock.accept();
			while(!servSock.isClosed()){
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void SendFilePart(FilePart fp) throws IOException{
		clientSocket.getOutputStream().write(fp.toArray());
		if(fp.isLast()){
			servSock.close();
		}
		 
	}

}
