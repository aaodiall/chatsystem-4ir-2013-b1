/**
 * 
 */
package chatSystemNetwork;

import java.net.InetAddress;

/**
 * This class contains all the messages that the local chat system can send to a remote application
 * @author joanna
 *
 */
public interface ToRemoteApp {
	
	/**
	 * connect to the chat system
	 * @param ack
	 */
	public void connect(boolean ack);
	
	/**
	 * disconnect from the chat system
	 */
	public void disconnect();
	
	/**
	 * use to send a text message to a single remote user
	 * @param recipient
	 * @param text2Send
	 */
	public void sendMsgText(InetAddress recipient, String text2Send);	
	
	/**
	 * use to send a proposition of file to a single remote user
	 * @param remote
	 * @param recipient
	 * @param fileName
	 * @param size
	 * @param idDemand
	 */
	public void sendPropositionFile(String remote, InetAddress recipient, String fileName, long size, int idDemand);
	
	/**
	 * use to answer a file transfer demand. Sends a confirmation of file
	 * @param recipientName
	 * @param recipientIP
	 * @param fileName
	 * @param answer
	 * @param idDemand
	 */
	public void sendConfirmationFile(String recipientName, InetAddress recipientIP, String fileName,boolean answer, int idDemand);
	
	/**
	 * use to send a part of file
	 * @param part
	 * @param idDemand
	 * @param isLast
	 */
	public void sendPart(byte[] part, int idDemand, boolean isLast);
}
