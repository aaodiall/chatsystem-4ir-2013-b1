package chatSystem.model;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

/**
 * Keeps and manages all the file's tranfers
 * @author Marjorie
 */
public class FileTransferts extends Model{
	
    private static FileTransferts instance;
    private Map<Integer, FileTransfertInformation> fileModel;

    /**
     * Private class' constructor
     */
    private FileTransferts() {
            this.fileModel = new HashMap<Integer, FileTransfertInformation>();
    }

    /**
     * Adding a new file's transfer
     * @param fileToSend
     * @param idRemoteSystem sender's id
     */
    public synchronized void addTransfert(File fileToSend, String idRemoteSystem) {
        
       FileTransfertInformation newFileTransfert = new FileSendingInformation(idRemoteSystem, fileToSend);
       this.fileModel.put(newFileTransfert.getId(), newFileTransfert);
        
       this.setChanged();
       this.notifyObservers(newFileTransfert);
       this.clearChanged();
    }
    
    /**
     * Adding a new file's transfer
     * @param name
     * @param size file's size
     * @param idRemoteSystem sender's id
     */
    public synchronized void addTransfert(int idTransfert, String name, long size, String idRemoteSystem, int portServer) {
        
       FileTransfertInformation newFileTransfert = new FileReceivingInformation(idTransfert, idRemoteSystem, size, name, portServer);
       this.fileModel.put(idTransfert, newFileTransfert);
        
       this.setChanged();
       this.notifyObservers(newFileTransfert);
       this.clearChanged();
    }

    /**
     * Remove a file's transfer
     * @param idTransfert transfer's id
     */
    public synchronized void deleteTransfert(int idTransfert) {
        this.fileModel.remove(idTransfert);
    }

    public FileTransfertInformation getFileTransfertInformation(int idTransfert) { 
        return this.fileModel.get(idTransfert);
    }

    public void setFileTransfertInformationState(int idTransfert, FileState state) {
        if (this.fileModel.containsKey((Integer)idTransfert)) {
            this.fileModel.get(idTransfert).setState(state);
            
            this.setChanged();
            this.notifyObservers(this.fileModel.get(idTransfert));
            this.clearChanged();
        }
    }
    
    /**
     * Static method to obtain an instance of the classe
     * @return FileTransferts' unique instance
     */
    public final static FileTransferts getInstance() {
            if (instance == null) {
                synchronized(FileTransferts.class) {
                    if (instance == null) {
                        instance = new FileTransferts();
                    }
                }
            }
            return instance;
    }       
}
