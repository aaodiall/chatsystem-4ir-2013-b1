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
	
	public void beginFileTransferEmission() {
		this.reset();
		this.setVisible(true);
		this.setText("File emission processing...");
		this.setMax(100);
	}
	
	public void beginFileTransferReception() {
		this.reset();
		this.setVisible(true);
		this.setText("File reception processing...");
		this.setMax(100);
	}

	public void finishFileTransferEmission() {
		this.setVisible(false);
		this.setText("File emission terminated");
		this.reset();
	}

	public void finishFileTransferReception() {
		this.setVisible(false);
		this.setText("File reception terminated");
		this.reset();
	}
}
