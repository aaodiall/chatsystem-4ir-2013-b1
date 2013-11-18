package chatSystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps and manages all the file's tranfers
 * @author Marjorie
 */
public class FileTransferts extends Model{
	
    private static FileTransferts instance;
    private List<FileTransfertInformation> FileModel;

    /**
     * Private class' constructor
     */
    private FileTransferts() {
            this.FileModel = new ArrayList<FileTransfertInformation>();
    }

    /**
     * Adding a new file's transfer
     * @param name file's name
     * @param extension file's extension
     * @param taille file's size
     * @param idRemoteSystem sender's id
     * @param idTransfert id given to the transfer
     */
    public void addTransfert(String name, long size, String idRemoteSystem, int idTransfert) {
            this.FileModel.add(new FileTransfertInformation(size,idRemoteSystem, name, idTransfert));
    }

    /**
     * Remove a file's transfer
     * @param idTransfert transfer's id
     */
    public void deleteTransfert(int idTransfert) {
    }


    public void getFileTransfertInformation() {
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
