/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * This class represents the states connected or disconnected of the local user
 * @author alpha
 *
 */
public class ModelStates extends Observable {
	private Boolean stateConnected;

	/**
	 * Constructor
	 */
	public ModelStates() {
		this.stateConnected = false;
	}

	/**
	 * 
	 * @return true if the local user is connected
	 */
	public Boolean isConnected() {
		return stateConnected;
	}

	/**
	 * change the local user's state
	 * @param stateConnected the new local user's state
	 */
	public void setState(boolean stateConnected) {
		this.stateConnected = stateConnected;
		setChanged();
		notifyObservers(stateConnected);
	}	
}
