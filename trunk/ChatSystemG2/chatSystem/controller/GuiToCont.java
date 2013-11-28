/*
 * Interface modelizing the messages that can be sent from the user interface to the controller
 */

package chatSystem.controller;

import java.io.File;

public interface GuiToCont {
    
    	public void performConnect(String username);
	
	public void performDisconnect();
        
        public void performSendMessageRequest(String message, String idRemoteSystem);
        
        public void performSendFileRequest(File fileToSend, String idRemoteSystem);
	
        public void performAcceptSuggestion(int idTransfert);
	
	public void performDeclineSuggestion(int idTransfert);
	
        public void performSaveFile(File fileToSave, int idTransfert);
    
}
