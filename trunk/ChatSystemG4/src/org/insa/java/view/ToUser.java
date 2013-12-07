package org.insa.java.view;

public interface ToUser {
	
	public String displayUsernameInputDialog();
	
	public void displayTalk(String talk);
	
	public void displayMessageInformation(String s);
	
	public void displayReceptionTransferInformation();
	
	public void hideReceptionTransferInformation();
	
	public void displayEmissionTransferInformation();
	
	public void hideEmissionTransferInformation();
	
	public void displayEmissionTransferPercent(int percent);
	
	public void displayReceptionTransferPercent(int percent);
	
	public void displayEmissionTransferError(String s);
	
	public void displayReceptionTransferError(String s);
}
