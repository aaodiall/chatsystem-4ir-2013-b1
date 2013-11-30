/**
 * Information about a file's transfert
 */

package chatSystem.model;

import java.io.File;

public abstract class FileTransfertInformation extends Model {

    protected static final int tailleSegment = 1024;
    
    private final int idTransfert;
    private final String idRemoteSystem;
    
    private FileState state;
    protected boolean isLast;
    private long sizeTransfered;
    
    protected File fileDescriptor;

    /**
     * Class' constructor to receive the file
     * @param idTransfert idTransfert choosed by the remote system
     * @param idRemoteSystem id of the sending remote system
     * @param fileToSend file's descriptor
     */
    public FileTransfertInformation(int idTransfert, String idRemoteSystem, File fileToReceive) {
        
        this.idRemoteSystem = idRemoteSystem;
        this.idTransfert = idTransfert;
        this.sizeTransfered = 0;
        this.isLast = false;        
        this.state = FileState.WAITANSWER;        
        this.fileDescriptor = fileToReceive;
    }
    
    /**
     * Class' constructor to send the file
     * @param idRemoteSystem id of the receiving remote system
     * @param fileToSend file's descriptor
     */
    public FileTransfertInformation(String idRemoteSystem, File fileToSend) {
        this(fileToSend.hashCode(),idRemoteSystem,fileToSend);
        // we set the idTransfert has fileToSend.hashCode() because it's unique in this application
    }

    /**
     * Obtaining the file's state
     * @return file's state
     */
    public FileState getState() {
        return this.state;
    }

    /**
     * Obtaining where the system is in the file's tranfer
     * @return transfer's progression
     */
    public long getProgression() {
        return sizeTransfered;
    }
    
    /**
     * Update the transfert's progresision
     * @param sizeTransfered size of the file's part that was transferred
     */
    protected void setProgression(long sizeTransfered){
        this.sizeTransfered = sizeTransfered;
        this.setChanged();
        this.notifyObservers(this.sizeTransfered);
        this.clearChanged();
    }

    /**
     * Say if the last part loaded is the last part of the file
     * @return isLast attribute
     */
    public boolean isLast() {
        return this.isLast;
    }
    
    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    /**
     * Obtaining the file's path in the system
     * @return file's path
     */
    public String getPath() {
        return this.fileDescriptor.getAbsolutePath();
    }

    /**
     * Setting the sending state of the file
     * @param state new file's state
     */
    public void setState(FileState state) {
        this.state = state;
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    }
    
    /**
     * Obtain the file transfert's id
     * @return file transfert's id
     */
    public int getId(){
        return this.idTransfert;
    }
    
    /**
     * Obtain the file's name 
     * @return file's name
     */
    public String getName(){
        return this.fileDescriptor.getName();
    }
    
    /**
     * Obtain the file's size
     * @return file's size
     */
    public long getSize(){
        return this.fileDescriptor.length();
    }
    
    /**
     * Obtain the communicating remote system's id
     * @return id of the remote system that is sending or receiving the file
     */
    public String getIdRemoteSystem(){
        return this.idRemoteSystem;
    }
}
