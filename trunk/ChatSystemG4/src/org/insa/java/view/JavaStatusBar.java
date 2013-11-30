package org.insa.java.view;

public abstract class JavaStatusBar {
	protected JavaFileTransferBar emissionBar;
	protected JavaFileTransferBar receptionBar;
	protected JavaStandardMessageBar messageBar;
	
	public void setEmissionBarValue(int i) {
		emissionBar.setValue(emissionBar.getValue() + i);
	}
	
	public void setReceptionBarValue(int i) {
		receptionBar.setValue(receptionBar.getValue() + i);
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
	
	public void beginFileTransferEmission(int fileSize) {
		emissionBar.beginFileTransferEmission(fileSize);
	}
	
	public void beginFileTransferReception(int fileSize) {
		receptionBar.beginFileTransferReception(fileSize);
	}
	
	public void finishFileTransferEmission() {
		emissionBar.finishFileTransferEmission();
	}
	
	public void finishFileTransferReception() {
		receptionBar.finishFileTransferReception();
	}
	
	public abstract Object getContainer();
	
}

