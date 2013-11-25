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
	public void addRecipient();
	
}
