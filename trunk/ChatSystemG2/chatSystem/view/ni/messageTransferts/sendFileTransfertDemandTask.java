package chatSystem.view.ni.messageTransferts;

/*
 * Represents the task to send a file transfert request to a given remote system
 */
public class sendFileTransfertDemandTask extends Task {

    private final String name;
    private final long size;
    private final String idRemoteSystem;
    private final int portClient;
    private final int idTransfert;

    /**
     * Class' constructor
     *
     * @param messageTransferts reference to the messageTransferts instance
     * which will fulfill the task
     * @param name file's name
     * @param size file's size
     * @param idRemoteSystem id of the remote system the request is to be sent
     * to
     * @param portClient port the transfert is going to use
     * @param idTransfert id of the current transfert
     */
    public sendFileTransfertDemandTask(MessageTransferts messageTransferts, String name, long size, String idRemoteSystem, int portClient, int idTransfert) {
        super(messageTransferts);
        this.name = name;
        this.size = size;
        this.idRemoteSystem = idRemoteSystem;
        this.portClient = portClient;
        this.idTransfert = idTransfert;
    }

    /**
     * Executing the task
     */
    @Override
    public void execute() {
        this.messageTransferts.sendFileTransfertDemand(name, size, idRemoteSystem, portClient, idTransfert);
    }
}
