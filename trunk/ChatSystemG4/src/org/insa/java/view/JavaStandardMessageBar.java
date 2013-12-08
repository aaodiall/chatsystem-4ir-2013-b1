package org.insa.java.view;

/**
 * Generic graphic class for java-based message bar.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public abstract class JavaStandardMessageBar {
	/**
	 * Set message bar text.
	 * @param s New text.
	 */
	public abstract void setText(String s);
	
	/**
	 * Get message bar container.
	 * @return container MessageBar container.
	 */
	public abstract Object getContainer();
}
