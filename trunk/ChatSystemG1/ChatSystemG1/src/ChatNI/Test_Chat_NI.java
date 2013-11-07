package ChatNI;

import java.net.*;

import chatSystemCommon.Hello;
public class Test_Chat_NI {

	public Test_Chat_NI() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		/*Receiver R = new Receiver(9876);
		R.run();
		*/
		
		Sender s = new Sender(new InetSocketAddress("localhost", 9876).getAddress(), 9876 );
		while(true){
			s.sendAway(new Hello("yeha", true));
		}
		
	}

}
