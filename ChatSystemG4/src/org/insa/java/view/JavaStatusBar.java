package org.insa.java.view;


public abstract class JavaStatusBar {
	protected JavaFileTransferBar emissionBar;
	protected JavaFileTransferBar receptionBar;
	protected JavaStandardMessageBar messageBar;
	protected JavaChatGUI chatGUI;
	
	public void setEmissionBarValue(int i) {
		emissionBar.setValue(i);
	}
	
	public void setReceptionBarValue(int i) {
		receptionBar.setValue(i);
	}
	
	public void setEmissionBarMax(int i) {
		emissionBar.setMax(i);
	}
	
	public void setReceptionBarMax(int i) {
		receptionBar.setMax(i);
	}
	
	public void setEmmissionBarText(String s) {
		emissionBar.setText(s);
	}
	
	public void setReceptionBarText(String s) {
		receptionBar.setText(s);
	}
	
	public void setMessageBarText(String s) {
		messageBar.setText(s);
	}
	
	public abstract Object getContainer();
}

