package chatSystem.model;

import java.util.ArrayList;
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
     * @param name file's name
     * @param size file's size
     * @param idRemoteSystem sender's id
     * @param idTransfert id given to the transfer
     */
    public synchronized void addTransfert(String name, long size, String idRemoteSystem, int idTransfert) {
        if (this.fileModel.containsKey(idTransfert)) {
            this.fileModel.remove(idTransfert);
        }
        this.fileModel.put(idTransfert, new FileTransfertInformation(size,idRemoteSystem, name, idTransfert));
        
       this.setChanged();
       this.notifyObservers(idTransfert);
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
