package chatSystem.view.ni.messageTransferts;

/**
 * Represents the task to send a goodbye message to everyone
 */
public class sendGoodbyeTask extends Task {

    /**
     * Class' constructor
     *
     * @param messageTransferts reference to the messageTransferts instance
     * which will fulfill the task
     */
    public sendGoodbyeTask(MessageTransferts messageTransferts) {
        super(messageTransferts);
    }

    /**
     * Executing the task
     */
    @Override
    public void execute() {
        this.messageTransferts.sendGoodbye();
    }
}
