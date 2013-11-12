/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelStateConnected extends Observable {
	private boolean stateConnected;

	
	/**
	 * @param stateConnected
	 */
	public ModelStateConnected() {
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
