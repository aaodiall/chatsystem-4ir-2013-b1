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
public interface GuiToCont {
    
    	public void performConnect(String username);
	
	public void performDisconnect();
        
        public void performSendMessageRequest(String message, String idRemoteSystem);
	
	//public void performFileSended();
    
}
