/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * This class represent the local user's user name
 * @author alpha
 *
 */
public class ModelUsername extends Observable{
	private String username;

	/**
	 * Constructor the user name is not initialized
	 */
	public ModelUsername() {}

	/**
	 * 
	 * @return the local user's user name
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * set the local user's user name
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
		setChanged();
		notifyObservers(username);
	}
}
