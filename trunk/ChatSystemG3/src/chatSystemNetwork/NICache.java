/**
 * 
 */
package chatSystemNetwork;

import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;

import chatSystemCommon.Hello;
import chatSystemModel.ModelFile;
import chatSystemModel.ModelUsername;

/**
 * @author joanna
 *
 */
public class NICache implements Observer{

	private String username;
	private InetAddress localBroadcast;
	private ArrayBlockingQueue<byte[]> parts;
	private Hello shello;
	
	NICache(){
		username = null;
		localBroadcast = null;
	}
	
	Hello getHello(boolean isAck){
		Hello h = SingletonHello.getInstance().getHello();
		h.setUsername(this.username);
		h.setIsAck(isAck);		
		return h;
	}
	
	String getUsername(){
		return this.username;
	}
	
	void setUsername(String username){
		this.username = username;
	}
	
	InetAddress getBroadcast(){
		return this.localBroadcast;
	}
	
	void setBroadcast(InetAddress localBroadcast){
		this.localBroadcast=localBroadcast;
	}
	
	public void update(Observable arg0, Object arg1) {
		if (arg0.getClass() == ModelUsername.class){
			this.username = ((String)arg1);
		}		
	}	
}