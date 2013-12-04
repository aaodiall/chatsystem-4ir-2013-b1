/**
 * 
 */
package chatSystemNetwork;

import java.net.InetAddress;

/**
 * @author joanna
 *
 */
public interface FromRemoteApp {
	public void helloReceived(String remoteUsername, InetAddress remoteIP, boolean isAck);	
	public void goodbyeReceived(String remoteUsername);	
	public void textReceived(String remoteUsername, String text);	
	public void fileTansfertDemandReceived(String remoteUsername, String fileName, long fileSize, int idDemand, int remotePort);	
	public void fileTansfertConfirmationReceived(String remoteUsername,int idDemand, boolean isAccepted);	
	public void fileTansfertCancelReceived(String remoteUsername,int idDemand);	
	public void filePartReceived(byte[] fileBytes, int numPort, boolean isLast);
}
