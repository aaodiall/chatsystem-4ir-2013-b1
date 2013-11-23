package chatSystem.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *  Scinder cette classe en deux -> une qui permet d'écrire un fichier recus et une qui permet de lire un fichier a envoyer ? 
 */
public abstract class FileTransfertInformation extends Model {

    private static final int tailleSegment = 1024;
    private static int idCpt;
    
    private final int idTransfert;
    private final String idRemoteSystem;
    private final int sizeTransfered;
    
    protected FileState state;
    protected boolean isLast;
    
    protected File fileDescriptor;

    /**
     * Class' constructor to send the file
     *
     * @param idRemoteSystem id of the sending remote system
     * @param fileToSend file's descriptor
     */
    public FileTransfertInformation(String idRemoteSystem, File fileToSend) {
        
        this.idRemoteSystem = idRemoteSystem;
        this.idTransfert = FileTransfertInformation.idCpt++;
        this.sizeTransfered = 0;
        this.isLast = false;
        
        this.fileDescriptor = fileToSend;
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
    public int getProgression() {
        return 0;
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
