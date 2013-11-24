/**
 * Keep and manage all the file's tranfers
 * This class implements the Singleton's pattern
 */

package chatSystem.model;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

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
     * @param fileToSend file about to be sent
     * @param idRemoteSystem sender's id
     * @return id of the new Transfert
     */
    public synchronized int addTransfert(File fileToSend, String idRemoteSystem) {
        
       FileTransfertInformation newFileTransfert = new FileSendingInformation(idRemoteSystem, fileToSend);
       this.fileModel.put(newFileTransfert.getId(), newFileTransfert);
       return newFileTransfert.getId();
    }
    
    /**
     * Adding a new file's transfer
     * @param idTransfert transfert's id
     * @param name file's name
     * @param size file's size
     * @param idRemoteSystem sender's id
     * @param portServer port the transfert is going to use
     */
    public synchronized void addTransfert(int idTransfert, String name, long size, String idRemoteSystem, int portServer) {
        
       FileTransfertInformation newFileTransfert = new FileReceivingInformation(idTransfert, idRemoteSystem, size, name, portServer);
       this.fileModel.put(idTransfert, newFileTransfert);
    }

    /**
     * Remove a file's transfer
     * @param idTransfert transfer's id
     */
    public synchronized void deleteTransfert(int idTransfert) {
        this.fileModel.remove(idTransfert);
    }

    /**
     * Get the information about a given file's transfert
     * @param idTransfert file transfert's id
     * @return instance of FileTransfertInformation corresponding to the id
     */
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
