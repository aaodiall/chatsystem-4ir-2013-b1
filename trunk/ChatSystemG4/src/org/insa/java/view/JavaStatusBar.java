package org.insa.java.view;

import org.insa.java.controller.ChatController;

public abstract class JavaStatusBar {
	protected JavaFileTransferBar emissionBar;
	protected JavaFileTransferBar receptionBar;
	protected JavaStandardMessageBar messageBar;
	protected ChatController chatController;
	
	public void setEmissionBarValue(int i) {
		//emissionBar.setValue(emissionBar.getValue() + i);
		emissionBar.setValue(i);
	}
	
	public void setReceptionBarValue(int i) {
		//receptionBar.setValue(receptionBar.getValue() + i);
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
	
	public void beginFileTransferEmission() {
		emissionBar.beginFileTransferEmission();
	}
	
	public void beginFileTransferReception() {
		receptionBar.beginFileTransferReception();
	}
	
	public void finishFileTransferEmission() {
		emissionBar.finishFileTransferEmission();
	}
	
	public void finishFileTransferReception() {
		receptionBar.finishFileTransferReception();
	}
	
	public abstract Object getContainer();

}

