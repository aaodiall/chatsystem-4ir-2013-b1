package chatSystem.view.gui;

import java.io.File;

/** Actions the chat system's user can start
 * Represents all the messages that can be sent by the local user and must be performed
 */

public interface FromUser {

    /**
     * Connection of the local user
     * @param username username chosen for the connection by the local user
     */
    public void connect(String username);

    /** Disconnection of the local user */
    public void disconnect();

    /**
     * Send a text message
     * @param message text message's content
     * @param idRemoteSystem id of the remote system the local user wants to
     * send the text message
     */
    public void sendMessageRequest(String message, String idRemoteSystem);

    /**
     * Save a file in the computer's memory
     * @param fileToSend file which is to be saved
     * @param idTransfert id of the file's transfert
     */
    public void saveFile(File fileToSend, int idTransfert);

     /**
     * Open the dialog window corresponding to the remote system the user wants
     * to talk to
     * @param idRemoteSystem id of the remote system
     */
    public void openDialogWindow(String idRemoteSystem);

    /**
     * Accept the file transfert's suggestion displayed to the local user
     * @param idTransfert id of the file's transfert
     */
    public void acceptSuggestion(int idTransfert);

    /**
     * Decline the file transfert's suggestion displayed to the local user
     * @param idTransfert id of the file's transfert
     */
    public void declineSuggestion(int idTransfert);

    /**
     * Send a file transfert's request to a given remote system
     * @param fileToSend file the user wants to send
     * @param idRemoteSystem id of the remote system the file is to be proposed to
     */
    public void sendFileRequest(File fileToSend, String idRemoteSystem);
}
