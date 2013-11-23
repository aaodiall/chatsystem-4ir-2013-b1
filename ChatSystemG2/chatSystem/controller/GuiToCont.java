/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatSystem.controller;

import java.io.File;

/**
 *
 * @author Marjorie
 */
public interface GuiToCont {
    
    	public void performConnect(String username);
	
	public void performDisconnect();
        
        public void performSendMessageRequest(String message, String idRemoteSystem);
        
        public void performSendFileRequest(File fileToSend, String idRemoteSystem);
	
        public void performAcceptSuggestion(int idTransfert);
	
	public void performDeclineSuggestion(int idTransfert);
	
        //public void performFileSended();
    
}
