/*
 * Represents the task to send a file transfert confirmation to a given remote system
 */

package chatSystem.view.ni.messageTransferts;


public class sendFileTransfertConfirmationTask extends Task {

    private final String idRemoteSystem;
    private final boolean isAccepted;
    private final int idTransfertRequest;
    /**
     * Class' constructor
     * @param messageTransferts reference to the messageTransferts instance which will fulfill the task
     * @param idRemoteSystem id of the remote system the confirmation is to be sent to
     * @param isAccepted boolean indicating whether or not the request has been accepted
     * @param idTransfertRequest id of the transfert request the user is answering 
     */
    public sendFileTransfertConfirmationTask(MessageTransferts messageTransferts, String idRemoteSystem, boolean isAccepted, int idTransfertRequest) {
        super( messageTransferts);
        this.idRemoteSystem = idRemoteSystem;
        this.isAccepted = isAccepted;
        this.idTransfertRequest = idTransfertRequest;
    }

    /**
     * Executing the task
     */
    @Override
    public void execute() {
        this.messageTransferts.sendFileTransfertConfirmation(isAccepted, idRemoteSystem, idTransfertRequest);
    }
}
