package chatSystem.view.ni.messageTransferts;

/*
 * Represents a task the messageTransferts has to fulfill
 */
public abstract class Task {

    protected MessageTransferts messageTransferts;

    /**
     * Class' constructor
     *
     * @param messageTransferts reference to the messageTransferts instance
     * which will fulfill the task
     */
    public Task(MessageTransferts messageTransferts) {
        this.messageTransferts = messageTransferts;
    }

    /**
     * Executing the task
     */
    public abstract void execute();
}
