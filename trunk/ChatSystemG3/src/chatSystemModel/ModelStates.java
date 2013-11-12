/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelStates extends Observable {
	private boolean stateConnected;

	
	/**
	 * @param stateConnected
	 */
	public ModelStates() {
		this.stateConnected = false;
	}

	public boolean isConnected() {
		return stateConnected;
	}

	public void setState(boolean stateConnected) {
		this.stateConnected = stateConnected;
		setChanged();
		notifyObservers();

	}

	
	
}
