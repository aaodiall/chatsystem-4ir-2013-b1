/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelUsername extends Observable{
	private String username;

	public ModelUsername() {
		this.username ="jojo";
	}

	public String getUsername() {
		return this.username;
	}
	

}
