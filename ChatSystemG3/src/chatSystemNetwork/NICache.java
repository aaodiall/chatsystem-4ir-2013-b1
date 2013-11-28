/**
 * 
 */
package chatSystemNetwork;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import chatSystemCommon.Hello;
import chatSystemModel.ModelUsername;

/**
 * @author joanna
 *
 */
public class NICache implements Observer{

	private String username;
	private InetAddress localBroadcast;
	private HashMap<String,Integer> nPorts;
	private HashMap<Integer,Integer> nDemands;
	
	NICache(){
		username = null;
		localBroadcast = null;
		this.nPorts = new HashMap<String,Integer>();
		this.nDemands = new HashMap<Integer,Integer>();

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
	
	void addPort(String remote, Integer portTransfert){
		this.nPorts.put(remote,portTransfert);
	}
	
	void removePort(String remote){
		this.nPorts.remove(remote);
	}
	
	Integer getPort(String remote){
		return this.nPorts.get(remote);
	}
	
	void addDemand(Integer port, Integer numDemand){
		this.nDemands.put(port,numDemand);
	}
	
	void removeDemand(Integer port){
		this.nDemands.remove(port);
	}
	
	Integer getDemand(Integer port){
		return this.nDemands.get(port);
	}
	
	public void update(Observable arg0, Object arg1) {
		if (arg0.getClass() == ModelUsername.class){
			this.username = ((String)arg1);
		}		
	}	
}
