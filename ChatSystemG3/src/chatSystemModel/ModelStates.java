/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelStates extends Observable{

	private boolean stateconnected;

	public boolean isConnected() {
		return stateconnected;
	}

	public void setState(boolean connected){
		this.stateconnected = connected;
	}
	
}