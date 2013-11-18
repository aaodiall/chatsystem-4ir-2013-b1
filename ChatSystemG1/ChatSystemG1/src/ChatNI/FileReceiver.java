package ChatNI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import chatSystemCommon.Message;

public class FileReceiver implements Runnable{
	private int port;
	private String ip;
	private ServerSocket servSock;
	private Socket clientSocket;
	public FileReceiver(String remoteUser, int portClient) {
		// TODO Auto-generated constructor stub
		this.port = portClient;
		this.ip = remoteUser;
		Thread t = new Thread();
		t.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.clientSocket = new Socket(ip, port);
			while(!clientSocket.isClosed()){
				byte[] mes = new byte[1024];
				clientSocket.getInputStream().read(mes);
				ReceiveFilePart(mes);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ReceiveFilePart(byte[] mes) throws IOException{
		Message m = Message.fromArray(mes);
		m.setUsername(m.getUsername() + "@" + ip);
		ChatNetwork.NotifyMessageReceive(m);
	}

}
