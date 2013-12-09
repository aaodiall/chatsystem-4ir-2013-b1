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
	
	/**
	 * this function is used when the chat system has received a Hello
	 * @param remoteUsername remote user's username
	 * @param remoteIP remote user's IP address
	 * @param isAck informs if the Hello is an acknowledgment or not
	 */
	public void helloReceived(String remoteUsername, InetAddress remoteIP, boolean isAck);	
	
	/**
	 * this function is used when the chat system has received a Goodbye
	 * @param remoteUsername remote user's username
	 */
	public void goodbyeReceived(String remoteUsername);	
	
	/**
	 * this function is used when the chat system has received a FileTransfertDemand
	 * @param remoteUsername remote user's username
	 * @param fileName name of the file
	 * @param fileSize size of the file
	 * @param idDemand id of the demand 
	 * @param remotePort port of the server
	 */
	public void fileTansfertDemandReceived(String remoteUsername, String fileName, long fileSize, int idDemand, int remotePort);	
	
	/**
	 * this function is used when the chat system has received a FileTransfertConfirmation
	 * @param remoteUsername remote user's username
	 * @param idDemand id of the demand
	 * @param isAccepted informs if the file is accepted or not
	 */
	public void fileTansfertConfirmationReceived(String remoteUsername,int idDemand, boolean isAccepted);	
	
	/**
	 * this function is used when the chat system has received a FileTransfertCancel
	 * @param remoteUsername remote user's username
	 * @param idDemand id of the demand
	 */
	//public void fileTansfertCancelReceived(String remoteUsername,int idDemand);	
	
	/**
	 * this function is used when the chat system has received a FilePart
	 * @param fileBytes byte array that represents a part of the file
	 * @param idDemand id of the demand
	 * @param isLast informs if the part is the last or not
	 */
	public void filePartReceived(byte[] fileBytes, int idDemand, boolean isLast);
}
