/**
 * 
 */
package chatSystemNetwork;

import java.net.InetAddress;

/**
 * @author joanna
 *
 */
public interface ToRemoteApp {
	public void connect(boolean ack);	
	public void disconnect();	
	public void sendMsgText(InetAddress recipient, String text2Send);	
	public void sendPropositionFile(String remote, InetAddress recipient, String fileName, long size, int idDemand, int nbParts);
	public void sendConfirmationFile(String recipientName, InetAddress recipientIP, String fileName,boolean answer, int idDemand);
	public void sendPart(byte[] part, int idDemand, boolean isLast);
	//public void sendFile(ArrayBlockingQueue<byte[]> parts,int idDemand);
}
