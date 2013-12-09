/**
 * 
 */
package chatSystemModel;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Observable;

/**
 * This class represents information about all connected people
 * @author alpha
 * 
 */
public class ModelListUsers extends Observable {
	private HashMap<String, InetAddress> listUsers;
	
	/**
	 * Constructor
	 */
	public ModelListUsers() {
		super();
		this.listUsers=new HashMap<String,InetAddress>();
	}
	
	/**
	 * 
	 * @return the connected people list
	 */
	public HashMap<String, InetAddress> getListUsers(){
		return this.listUsers;
	}
	
	/**
	 * add a connected people in the list
	 * @param username user name of the person
	 * @param addressIP IP address of the remote machine
	 */
	public void addUsernameList(String username,InetAddress addressIP){
		listUsers.put(username,addressIP);
		setChanged();
		notifyObservers(listUsers);
	}
	
	/**
	 * remove a person in the list, means that this person is not connected anymore
	 * @param username
	 */
	public void removeUsernameList(String username){
		this.listUsers.remove(username);
		setChanged();
		notifyObservers(listUsers);
	}
	
	/**
	 * 
	 * @param username
	 * @return true if the user name is present in the list
	 */
	public boolean isInListUsers(String username){
		return this.listUsers.containsKey(username);
	}

	
	/**
	 * remove all user names
	 */
	public void clearListUsers(){
		this.listUsers.clear();
	}	
	
}
