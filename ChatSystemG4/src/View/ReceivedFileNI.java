package View;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import chatSystemCommon.Message;
import Controller.ChatController;

public final class ReceivedFileNI extends Thread {
	private static ReceivedFileNI instance = null;
	
	private ChatController chatController;
	
	private ServerSocket serverSocket;
    private Socket socket;
    private InputStream inputStream;	
	
	private ReceivedFileNI(ChatController chatController, int portClient) {
		this.chatController = chatController;
		try {
			serverSocket = new ServerSocket(portClient);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final static ReceivedFileNI getInstance(ChatController chatController, int portClient) {
		if(ReceivedFileNI.instance == null) {
			synchronized(ReceivedFileNI.class) {
				if(ReceivedFileNI.instance == null)
					ReceivedFileNI.instance = new ReceivedFileNI(chatController,portClient);
			}
		}
		return ReceivedFileNI.instance;
	}
	
	public byte[] toByteArray(InputStream is) throws IOException{
		/*
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int reads = is.read();
       
        while(reads != -1){
            baos.write(reads);
            reads = is.read();
        }
        return baos.toByteArray();
    	*/
		return IOUtils.toByteArray(is);
    }

	public void run() { 
        while(true) {
            try {
				socket = serverSocket.accept();	
				System.out.println(socket.toString());
				inputStream = socket.getInputStream();
				//byte[] buf = 
				System.out.println(Message.fromArray(toByteArray(inputStream)));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
}
