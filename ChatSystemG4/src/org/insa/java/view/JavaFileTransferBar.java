package org.insa.java.view;

public abstract class JavaFileTransferBar {		
	public abstract void setVisible(boolean b);
	
	public abstract void setText(String s);
	
	public abstract void setMax(int i);
	
	public abstract void setValue(int i);
	
	public abstract int getValue();
	
	public abstract void reset();
	
	public abstract Object getContainer();
	
	public abstract Object getCancelbutton();
}
