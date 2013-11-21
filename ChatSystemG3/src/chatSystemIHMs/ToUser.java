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
	public void displayMessage(String text, String remote);
	public void notifyRemoteConnection(String remote);
	public void notifyRemoteDisconnection(String remote);

}
