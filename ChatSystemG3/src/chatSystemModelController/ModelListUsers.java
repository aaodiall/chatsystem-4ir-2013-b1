/**
 * 
 */
package chatSystemModelController;

import java.util.HashMap;
import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelListUsers extends Observable {
	private HashMap listUsers;

	public ModelListUsers() {
		super();
		this.listUsers=new HashMap();
	}
	
	public void AddUsernameList(String username,String addressIP){
		listUsers.put(username,addressIP);
	}

}
