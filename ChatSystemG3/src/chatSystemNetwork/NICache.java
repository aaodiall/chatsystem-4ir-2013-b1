/**
 * 
 */
package chatSystemNetwork;

import java.net.InetAddress;
import java.util.HashMap;
import chatSystemCommon.Hello;

/**
 * This class is a local memory for the ChatNI
 * @author joanna
 *
 */
public class NICache {

	private String username; // nom du local user
	private InetAddress localBroadcast; // address IP de broadcast du local user
	private HashMap<Integer,Integer> remoteDemands; // key = idDemand , value = portTCPServer distant
	private HashMap<Integer,ChatNIStreamSender> senders; // key = idDemand , value = ChatNIStreamSender
	
	/**
	 * Constructor of NICache
	 */
	NICache(){
		username = null;
		localBroadcast = null;
		this.remoteDemands = new HashMap<Integer,Integer>();
		this.senders = new HashMap<Integer,ChatNIStreamSender>(); 
	}
	
	/**
	 * add a sender in the cache
	 * @param idDemand id of the FileTransfertDemand sent
	 * @param sender object of type ChatNIStreamSender associated with idDemand 
	 */
	void addSender(int idDemand, ChatNIStreamSender sender){
		this.senders.put(idDemand, sender);
	}
	
	/**
	 * get the ChatNIStream
	 * @param idDemand id of a FileTransfert demand
	 * @return object of type ChatNIStreamSender which match with the idDemand
	 */
	ChatNIStreamSender getSender(int idDemand){
		return this.senders.get(idDemand);
	}
	
	/**
	 * remove the ChatNIStreamSender that match with idDemand
	 * @param idDemand id of a FileTransferDemand sent
	 */
	void removeSender(int idDemand){
		this.senders.remove(idDemand);
	}
	
	/**
	 * get an instance of an object of type Hello
	 * @param isAck
	 * @return object of type Hello with the local username and the value of isAck
	 */
	Hello getHello(boolean isAck){
		Hello h = SingletonHello.getInstance().getHello();
		h.setUsername(this.username);
		h.setIsAck(isAck);		
		return h;
	}
	
	/**
	 * 
	 * @return local user's username
	 */
	String getUsername(){
		return this.username;
	}
	
	/**
	 * 
	 * @param username local user's username 
	 */
	void setUsername(String username){
		this.username = username;
	}
	
	/**
	 * 
	 * @return local user's ip broadcast address 
	 */
	InetAddress getBroadcast(){
		return this.localBroadcast;
	}
	
	/**
	 * 
	 * @param localBroadcast local user's ip broadcast address
	 */
	void setBroadcast(InetAddress localBroadcast){
		this.localBroadcast=localBroadcast;
	}
	
	/**
	 * 
	 * @param idDemand
	 * @param port
	 */
	void addRemotePort(int idDemand, int port){
		this.remoteDemands.put(idDemand,port);
		System.out.println("NICache --> demand "+ idDemand+ "  remote "+this.remoteDemands.get(idDemand));
	}
	
	
	/**
	 * 
	 * @param idDemand
	 */
	void removeRemotePort(int idDemand){
		this.remoteDemands.remove(idDemand);
	}
	
	
	/**
	 * 
	 * @param idDemand
	 * @return related port
	 */
	Integer getRemotePort(int idDemand){
		return this.remoteDemands.get(idDemand);
	}
	
	
	/**
	 * cleans the cache
	 */
	void clear(){
		this.username=null;
		this.localBroadcast=null;
		this.remoteDemands.clear();
		this.remoteDemands=null;
		this.senders.clear();
		this.senders=null;
	}
}
