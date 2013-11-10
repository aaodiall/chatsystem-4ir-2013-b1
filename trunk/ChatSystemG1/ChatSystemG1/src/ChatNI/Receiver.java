package ChatNI;

import java.io.IOException;

import chatSystemCommon.Message;

public class Receiver implements Runnable{

	public Receiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

		
	public void ReceiveMessage(byte[] MessageToConvert){
		try {
			ChatNetwork.NotifyMessageReceive(Message.fromArray(MessageToConvert));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
