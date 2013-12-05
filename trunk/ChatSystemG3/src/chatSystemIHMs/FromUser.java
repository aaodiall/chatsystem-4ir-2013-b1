/**
 * 
 */
package chatSystemIHMs;

/**
 * represents all the actions initiate by the user 
 * @author Alpha DIALLO & Joanna VIGNE
 * version 1.0
 *
 */
public interface FromUser{
	public void connect();
	public void disconnect();
	public void sendMessage(String remote);
	public void sendFile(String remote);
	public void receiveFile(String remote, int answer);
}
