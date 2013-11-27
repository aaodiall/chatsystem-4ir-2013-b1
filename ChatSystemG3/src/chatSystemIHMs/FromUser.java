/**
 * 
 */
package chatSystemIHMs;

/**
 * @author alpha
 *
 */
public interface FromUser{
	
	public void connect();
	public void disconnect();
	public void sendMessage(String remote);
	public void sendFile(String remote);
	public void receiveFile(String remote, boolean answer);
	public void addRecipient(String remote);
	public void removeRecipient(String remote);
}
