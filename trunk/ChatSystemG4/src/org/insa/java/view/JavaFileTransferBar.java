package org.insa.java.view;

/**
 * Generic graphic class for java-based file transfer bar.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public abstract class JavaFileTransferBar {		
	/**
	 * Set the file bar visible
	 * @param b True file bar visile, file bar invisible otherwise.
	 */
	public abstract void setVisible(boolean b);
	
	/**
	 * Set file bar text.
	 * @param s New text.
	 */
	public abstract void setText(String s);
	
	/**
	 * Set file bar max value.
	 * @param i New max value.
	 */
	public abstract void setMax(int i);
	
	/**
	 * Set file bar value.
	 * @param i New value.
	 */
	public abstract void setValue(int i);
	
	/**
	 * Get file bar value.
	 * @return value File bar value.
	 */
	public abstract int getValue();
	
	/**
	 * Reset file bar.
	 */
	public abstract void reset();
	
	/**
	 * Get file bar container.
	 * @return container File bar container.
	 */
	public abstract Object getContainer();
	
	/**
	 * Get file bar cancel button.
	 * @return cancelButton Return file bar cancel button.
	 */
	public abstract Object getCancelbutton();
}
