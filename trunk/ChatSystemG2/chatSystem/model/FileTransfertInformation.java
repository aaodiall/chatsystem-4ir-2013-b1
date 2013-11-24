package chatSystem.model;

import java.io.File;

public abstract class FileTransfertInformation extends Model {

    protected static final int tailleSegment = 1024;
    
    private final int idTransfert;
    private final String idRemoteSystem;
    
    private FileState state;
    protected boolean isLast;
    protected long sizeTransfered;
    
    protected File fileDescriptor;

    /**
     * Class' constructor to send the file
     *
     * @param idTransfert idTransfert choosed (by us or the remote system)
     * @param idRemoteSystem id of the sending remote system
     * @param fileToSend file's descriptor
     */
    public FileTransfertInformation(int idTransfert, String idRemoteSystem, File fileToSend) {
        
        this.idRemoteSystem = idRemoteSystem;
        this.idTransfert = idTransfert;
        this.sizeTransfered = 0;
        this.isLast = false;
        
        this.state = FileState.WAITANSWER;
        
        this.fileDescriptor = fileToSend;
    }
    
    public FileTransfertInformation(String idRemoteSystem, File fileToSend) {
        this(fileToSend.hashCode(),idRemoteSystem,fileToSend);
        // we set the idTransfert has fileToSend.hashCode() because it's unique in this application
        System.out.println(idTransfert +"      "+this);
    }

    /**
     * Obtaining the file's state
     *
     * @return file's state
     */
    public FileState getState() {
        return this.state;
    }

    /**
     * Obtaining where the system is in the file's tranfer
     *
     * @return transfer's progression
     */
    public long getProgression() {
        return sizeTransfered;
    }

    /**
     * Say if the last part loaded is the last part of the file
     * @return isLast attribute
     */
    public boolean isLast() {
        return this.isLast;
    }
    
    public boolean setIsLast(boolean isLast) {
        return this.isLast = isLast;
    }

    /**
     * Obtaining the file's path in the system
     *
     * @return file's path
     */
    public String getPath() {
        return this.fileDescriptor.getAbsolutePath();
    }

    /**
     * Setting the sending state of the file
     *
     * @param state new file's state
     */
    public void setState(FileState state) {
        this.state = state;
    }
    
    public int getId(){
        return this.idTransfert;
    }
    
    public String getName(){
        return this.fileDescriptor.getName();
    }
    
    public long getSize(){
        return this.fileDescriptor.getTotalSpace();
    }
    
    public String getIdRemoteSystem(){
        return this.idRemoteSystem;
    }
}
