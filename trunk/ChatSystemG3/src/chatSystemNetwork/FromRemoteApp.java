/**
 * 
 */
package chatSystemNetwork;

import java.net.InetAddress;

/**
 * This class contains all the messages that can be received from a remote application
 * @author joanna
 *
 */
public interface FromRemoteApp {
	
	/**
	 * this method is used when the chat system has received a Hello
	 * @param remoteUsername remote login
	 * @param remoteIP remote user's IP address
	 * @param isAck informs if the Hello is an acknowledgment or not
	 */
	public void helloReceived(String remoteUsername, InetAddress remoteIP, boolean isAck);	
	
	/**
	 * this method is used when the chat system has received a Goodbye
	 * @param remoteUsername remote login
	 */
	public void goodbyeReceived(String remoteUsername);	
	
	/**
	 * this method is used when the chat system receives a text message
	 * @param remoteUsername remote login
	 * @param text received text
	 */
	public void textReceived(String remoteUsername, String text);
	
	/**
	 * this method is used when the chat system has received a FileTransfertDemand
	 * @param remoteUsername remote login
	 * @param fileName name of the file
	 * @param fileSize size of the file
	 * @param idDemand id of the demand 
	 * @param remotePort port of the server
	 */
	public void fileTansfertDemandReceived(String remoteUsername, String fileName, long fileSize, int idDemand, int remotePort);	
	
	/**
	 * this function is used when the chat system has received a FileTransfertConfirmation
	 * @param remoteUsername remote login
	 * @param idDemand id of the demand
	 * @param isAccepted informs if the file is accepted or not
	 */
	public void fileTansfertConfirmationReceived(String remoteUsername,int idDemand, boolean isAccepted);	
	
	/**
	 * this function is used when the chat system has received a FilePart
	 * @param fileBytes byte array that represents a part of the file
	 * @param idDemand id of the demand
	 * @param isLast informs if the part is the last or not
	 */
	public void filePartReceived(byte[] fileBytes, int idDemand, boolean isLast);
}
