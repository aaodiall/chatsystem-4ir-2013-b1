package View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Controller.ChatController;

public final class ReceivedFileNI implements Runnable {
	private static ReceivedFileNI instance = null;
	
	private ChatController chatController;
	
	private ServerSocket serverSocket;
    private Socket socket;
    private InputStream inputStream;	
	
	private ReceivedFileNI(ChatController chatController) {
		this.chatController = chatController;
		try {
			serverSocket = new ServerSocket(16001);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public final static ReceivedFileNI getInstance(ChatController chatController) {
		if(ReceivedFileNI.instance == null) {
			synchronized(ReceivedFileNI.class) {
				if(ReceivedFileNI.instance == null)
					ReceivedFileNI.instance = new ReceivedFileNI(chatController);
			}
		}
		return ReceivedFileNI.instance;
	}

	public void run() { 
        while(true) {
            try {
				socket = serverSocket.accept();
				inputStream = socket.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}         
            
            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            //bw.write("Blabla");
            //bw.flush();
            //socket.close();
        }

		/*
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(16001, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (true) {
				//System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");
				//Receive a packet
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);

				//Packet received
				//System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
				//System.out.println(getClass().getName() + ">>>Packet received; data: " + Message.fromArray(packet.getData()));
				
				chatController.receivedMessage(packet.getAddress(),Message.fromArray(packet.getData()));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			socket.close();
		}
		*/
	}
}
