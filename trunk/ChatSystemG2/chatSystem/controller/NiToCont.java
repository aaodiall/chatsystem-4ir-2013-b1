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
    	public void performHelloReceived(Hello msg, String ip);

	public void GoodbyeReceived(String idRemoteSystem);
	
	/*public void performMessageReceived();
	
	public void performSendMessageRequest(String message);
	
	public void performSendFileRequest();
	
	public void performSuggestionReceived(FileTransfertDemand suggestion);
	
	public void performAcceptSuggestion();
	
	public void performDeclinedSuggestion();
	
	public void performFileReceived(File f);*/
}
