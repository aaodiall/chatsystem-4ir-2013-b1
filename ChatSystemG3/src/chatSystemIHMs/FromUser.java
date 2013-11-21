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
	public void sendMessage();
	public void sendFile();
	public void addRecipient();
	
}
