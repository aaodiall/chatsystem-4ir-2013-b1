/*
 * Interface modelizing the messages that can be sent from the user interface to the controller
 */

package chatSystem.controller;

import java.io.File;

public interface GuiToCont {

    /**
     * Perform the user's attempt to connect himself
     * @param username username the user chose
     */
    public void performConnect(String username);

    /**
     * Perform the user's attempt to get disconnected
     */
    public void performDisconnect();

    /**
     * Send a text message to a given remote system
     * @param message text message' content
     * @param idRemoteSystem id of the remote system the text message is to be sent to
     */
    public void performSendMessageRequest(String message, String idRemoteSystem);

    /**
     * Perform the sending of a file transfert deman to a given remote system
     * @param fileToSend file the user wants to send
     * @param idRemoteSystem id of the remote system the request is to be sent to
     */
    public void performSendFileRequest(File fileToSend, String idRemoteSystem);

    /**
     * Perform the consent of a file transfert demande by the local user
     * @param idTransfert id of the transfert which was accepted
     */
    public void performAcceptSuggestion(int idTransfert);

    /**
     * Perform the refusal of a file transfert demande by the local user
     * @param idTransfert id of the transfert which was refused
     */
    public void performDeclineSuggestion(int idTransfert);

    /**
     * Perform the copy of the file
     * @param fileToSave file which is to be saved
     * @param idTransfert transfert's id
     */
    public void performSaveFile(File fileToSave, int idTransfert);

}
