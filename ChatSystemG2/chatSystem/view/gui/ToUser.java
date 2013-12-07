package chatSystem.view.gui;

import chatSystem.model.FileReceivingInformation;
import chatSystem.model.FileTransfertInformation;
import java.util.List;

/**
 * Represents all the messages that can be sent to the local user
 */
public interface ToUser {

    /**
     * Modify the interface to show the user he is disconnected
     */
    public void disconnected();

    /**
     * Modify the interface to show the user he is connected
     */
    public void connected();

    /**
     * Display an error message to show the user the connection got wrong
     */
    public void displayConnectionErrorNotification();

    /**
     * Display an error message to show the user there was an error during the
     * file transfert
     *
     * @param idRemoteSystem contact
     * @param fileName name of the file
     */
    public void displayFileTransfertErrorNotification(String idRemoteSystem, String fileName);

    /**
     * Modify the interface to show the user the progression of his file
     * transfert
     *
     * @param tmp information about the file transfert
     */
    public void displayFileTransfertProgression(FileTransfertInformation tmp);

    /**
     * Modify the interface to reset the progression of a file transfert
     *
     * @param tmp information about the file transfert
     */
    public void resetFileTransfertProgression(FileTransfertInformation tmp);

    /**
     * Modify the interface to show the user a file transfert's suggestion
     *
     * @param tmp
     */
    public void displayFileSuggestion(FileReceivingInformation tmp);

    /**
     * Modify the interface to show the user the dialog window of the remote
     * system he wishes to communicate with
     *
     * @param contact
     */
    public void displayDialogWindow(String contact);

    /**
     * Modify the interface to update the list of available contacts
     *
     * @param newList new list of available contact
     * @throws GUIException security in case the update was launched before
     * creating the user list window
     */
    public void listUser(List<String> newList) throws GUIException;

    /**
     * Update the interface corresponding to a given Remote Sytem by adding a
     * new message
     *
     * @param idRemoteSystem
     * @param newMessage
     */
    public void displayMessage(String idRemoteSystem, String newMessage);
}
