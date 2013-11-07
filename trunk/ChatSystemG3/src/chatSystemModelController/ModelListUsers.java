/**
 * 
 */
package chatSystemModelController;

import java.util.HashMap;

/**
 * @author alpha
 *
 */
public class ModelListUsers {
	private HashMap listUsers;

	public ModelListUsers() {
		super();
		this.listUsers=new HashMap();
	}
	
	public void AddUsernameList(String username,String addressIP){
		listUsers.put(username,addressIP);
	}

}
