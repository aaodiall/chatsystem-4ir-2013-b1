package org.insa.java.view;

/**
 * Actions from application to user.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public interface ToUser {
	/**
	 * Allows the local user to enter his name.
	 * @return name Local user name.
	 */
	public String displayUsernameInputDialog();
	
	/**
	 * Display a talk.
	 * @param talk String representation of a talk.
	 */
	public void displayTalk(String talk);
	
	/**
	 * Display an information into message bar.
	 * @param s Information to display.
	 */
	public void displayMessageInformation(String s);
	
	/**
	 * Display basics informations into file reception bar when a file reception begins.
	 */
	public void displayReceptionTransferInformation();
	
	/**
	 * Hide informations from file reception bar when file transfer ends.
	 */
	public void hideReceptionTransferInformation();
	
	/**
	 * Display basics informations into file emission bar when a file emission begins.
	 */
	public void displayEmissionTransferInformation();
	
	/**
	 * Hide informations from file emission bar when file transfer ends.
	 */
	public void hideEmissionTransferInformation();
	
	/**
	 * Update emission value.
	 * @param percent Percent of the file sent.
	 */
	public void displayEmissionTransferPercent(int percent);
	
	/**
	 * Update reception value.
	 * @param percent Percent of the file received.
	 */
	public void displayReceptionTransferPercent(int percent);
	
	/**
	 * Display an error message into file emission transfer bar.
	 * @param s Error message
	 */
	public void displayEmissionTransferError(String s);
	
	/**
	 * Display an error message into file reception transfer bar.
	 * @param s Error message
	 */
	public void displayReceptionTransferError(String s);
}
