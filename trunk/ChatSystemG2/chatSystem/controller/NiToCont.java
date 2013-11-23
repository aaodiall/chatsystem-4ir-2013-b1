/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
	
	public void performFilePartReceived(byte[] filePart, boolean isLast);
        
        public void performFileSended(int idTransfert, String idRemoteSystem);
}
