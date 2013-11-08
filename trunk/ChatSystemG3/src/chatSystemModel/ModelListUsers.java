/**
 * 
 */
package chatSystemModel;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelListUsers extends Observable {
	private HashMap<String, InetAddress> listUsers;

	public ModelListUsers() {
		super();
		this.listUsers=new HashMap<String,InetAddress>();
	}
	
	public void AddUsernameList(String username,InetAddress addressIP){
		listUsers.put(username,addressIP);
	}
	
	public boolean isInListUsers(String username){
		return this.listUsers.containsKey(username);
	}

	
	
}
