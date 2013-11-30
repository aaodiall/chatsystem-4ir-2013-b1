package chatSystem.view.gui;

/**
 * Exception that can be used when a problem occurred in the windows
 */
public class GUIException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public GUIException() {
	}
	
	public GUIException(String message) {
		super(message);
	}

}
