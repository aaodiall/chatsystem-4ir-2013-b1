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
	public void closeConnection();
	public void openWindowCommunicate(String RemoteUsername);
	public void displayMessage(String text, String remote);
	public void proposeFile(String remote, String file, long size);
	public void notifyAnswerFile(String remote, String file,boolean answer);
	public void displayCancelFile(String remote,String file);
	public void notifyRemoteConnection(String remote);
	public void notifyRemoteDisconnection(String remote);
	public void openInterfaceDialogFile();

}
