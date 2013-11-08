/**
 * 
 */
package chatSystemModel;

import java.util.HashMap;
import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelListUsers extends Observable {
	private HashMap<String,String> listUsers;

	public ModelListUsers() {
		super();
		this.listUsers=new HashMap<String,String>();
	}
	
	public void AddUsernameList(String username,String addressIP){
		listUsers.put(username,addressIP);
	}
	
	public boolean isInListUsers(String username){
		return this.listUsers.containsKey(username);
	}

	
	
}
