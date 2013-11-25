/**
 * 
 */
package chatSystemIHMs;

/**
 * @author alpha
 *
 */
public interface ToUser {
	
	public void initConnection();
	public void openWindowCommunicate(String RemoteUsername);
	public void displayMessage(String text, String remote);
	public void proposeFile(String remote, String file, long size);
	public void displayOkFile(String remote, String file);
	public void displayKoFile(String remote, String file);
	public void displayCancelFile(String remote,String file);
	public void notifyRemoteConnection(String remote);
	public void notifyRemoteDisconnection(String remote);
	public void openInterfaceDialogFile();

}
