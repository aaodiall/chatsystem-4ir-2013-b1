/*
 * Represents a task to send a hello message, to everyone or to one remote system
 */

package chatSystem.view.ni.messageTransferts;

/**
 *
 * @author Marjorie
 */
public class sendHelloTask extends Task {

    private final String ip;
    
    /**
     * Class' constructor
     * @param messageTransferts reference to the messageTransferts instance which will fulfill the task
     * @param ip ip adress of the contact the hello message is to be sent to
     */
    public sendHelloTask(MessageTransferts messageTransferts, String ip) {
        super(messageTransferts);
        this.ip = ip;
    }

    /**
     * Class' constructor
     * @param messageTransferts reference to the messageTransferts instance which will fulfill the task
     */
    public sendHelloTask(MessageTransferts messageTransferts) {
        super(messageTransferts);
        this.ip = null;
    }
    
   /**
    * Executing the task
    */
    @Override
    public void execute() {
        if (ip == null) {
            this.messageTransferts.sendHello();
        } else {
            this.messageTransferts.sendHello(ip);
        }
    }
}
