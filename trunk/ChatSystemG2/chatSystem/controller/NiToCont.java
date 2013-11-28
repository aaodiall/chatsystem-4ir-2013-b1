/*
 * Interface modelizing the messages that can be sent from the network interface to the controller
 */

package chatSystem.controller;

/**
 *
 * @author Marjorie
 */
public interface NiToCont {
    	public void performHelloReceived(String username, String ip, boolean isAck);

	public void performGoodbyeReceived(String idRemoteSystem);
	
	public void performMessageReceived(String msg, String idRemoteSystem);
	
        public void performMessageSent(String message, String idRemoteSystem);
	
	public void performSuggestionReceived(String name, long size, String idRemoteSystem, int idTransfert, int portServer);
        
        public void performConfirmationReceived(String idRemoteSystem, int idTransfert, boolean accepted);
	
	public void performFilePartReceived(int idTransfert, byte[] filePart, boolean isLast);
        
        public void performFileSended(int idTransfert, String idRemoteSystem);
        
        public void performConnectionError();
        
        public void performFileTransfertError(int idTransfert);
}
