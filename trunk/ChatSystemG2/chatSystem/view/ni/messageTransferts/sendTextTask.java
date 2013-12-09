package chatSystem.view.ni.messageTransferts;

/**
 * Represents the task to send a text message to a given remote system
 */
public class sendTextTask extends Task {

    private final String text;
    private final String idRemoteSystem;

    /**
     * Class' constructor
     *
     * @param messageTransferts reference to the messageTransferts instance
     * which will fulfill the task
     * @param idRemoteSystem id of the remote system the text message is to be
     * sent to
     * @param text text message's content
     */
    public sendTextTask(MessageTransferts messageTransferts, String idRemoteSystem, String text) {
        super(messageTransferts);
        this.text = text;
        this.idRemoteSystem = idRemoteSystem;
    }

    /**
     * Executing the task
     */
    @Override
    public void execute() {
        this.messageTransferts.sendTextMessage(idRemoteSystem, text);
    }
}
