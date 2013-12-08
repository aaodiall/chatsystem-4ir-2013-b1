package org.insa.java.view;

/**
 * Generic graphic class for java-based status bar.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public abstract class JavaStatusBar {
	protected JavaFileTransferBar emissionBar;
	protected JavaFileTransferBar receptionBar;
	protected JavaStandardMessageBar messageBar;
	protected JavaChatGUI chatGUI;
	
	/**
	 * Set the value of the emission bar.
	 * @param i New value.
	 */
	public void setEmissionBarValue(int i) {
		emissionBar.setValue(i);
	}
	
	/**
	 * Set the value of the reception bar.
	 * @param i New value.
	 */
	public void setReceptionBarValue(int i) {
		receptionBar.setValue(i);
	}
	
	/**
	 * Set the max value of the emission bar.
	 * @param i New max value
	 */
	public void setEmissionBarMax(int i) {
		emissionBar.setMax(i);
	}
	
	/**
	 * Set the max value of the reception bar.
	 * @param i Ne max value.
	 */
	public void setReceptionBarMax(int i) {
		receptionBar.setMax(i);
	}
	
	/**
	 * Set emission bar text.
	 * @param s New text.
	 */
	public void setEmmissionBarText(String s) {
		emissionBar.setText(s);
	}
	
	/**
	 * Set reception bar text.
	 * @param s New text.
	 */
	public void setReceptionBarText(String s) {
		receptionBar.setText(s);
	}
	
	/**
	 * Set message bar text.
	 * @param s New text.
	 */
	public void setMessageBarText(String s) {
		messageBar.setText(s);
	}
	
	/**
	 * Get status bar container
	 * @return container Status bar container.
	 */
	public abstract Object getContainer();
}
