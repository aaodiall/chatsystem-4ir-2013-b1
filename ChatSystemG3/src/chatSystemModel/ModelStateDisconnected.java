/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelStateDisconnected extends Observable{
	private boolean stateDisconnected;
	/**
	 * @param stateDisconnected
	 */
	public ModelStateDisconnected() {
		this.stateDisconnected = false;
	}

	public boolean isDisconnected() {
		return stateDisconnected;
	}

	public void setState(boolean stateDisconnected) {
		this.stateDisconnected = stateDisconnected;
		setChanged();
		notifyObservers();

	}
}
