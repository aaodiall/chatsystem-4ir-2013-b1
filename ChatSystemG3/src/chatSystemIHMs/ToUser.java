/**
 * 
 */
package chatSystemIHMs;

/**
 * represents 
 * @author Alpha DIALLO & Joanna VIGNE
 * version 1.0
 *
 */
public interface ToUser {
	public void initConnection();
	public void openWindowCommunicate(String RemoteUsername);
	public void displayMessage(String text, String remote);
	public void proposeFile(String remote, String file, long size);
	public void openInterfaceDialogFile();
	public void notifyFileReceived(String remote);
}
