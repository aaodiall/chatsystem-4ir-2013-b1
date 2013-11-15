/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatSystem.controller;

import chatSystemCommon.*;

/**
 *
 * @author Marjorie
 */
public interface NiToCont {
    	public void performHelloReceived(String username, String ip);

	public void performGoodbyeReceived(String idRemoteSystem);
	
	public void performMessageReceived(String msg, String idRemoteSystem);
	
        public void performSentMessageToRemoteSystem(String message, String idRemoteSystem);
        
	/*public void performSendFileRequest();
	
	public void performSuggestionReceived(FileTransfertDemand suggestion);
	
	public void performAcceptSuggestion();
	
	public void performDeclinedSuggestion();
	
	public void performFileReceived(File f);*/
}
