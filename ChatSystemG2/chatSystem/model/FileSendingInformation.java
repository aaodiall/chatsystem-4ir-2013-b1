/**
 * Information about a file that is being sent by the local user
 */

package chatSystem.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSendingInformation extends FileTransfertInformation {

    private BufferedInputStream readerBuffer;
    private FileInputStream reader;
    private byte [] filePart;
    private final byte [] filePartInit;

    /**
     * Class' constructor
     * @param idRemoteSystem id of the remote system the file is being sent to
     * @param fileToSend file which is to be sent 
     */
    public FileSendingInformation(String idRemoteSystem, File fileToSend) {
        super(idRemoteSystem, fileToSend);
        
        this.filePart = new byte[tailleSegment];
        this.filePartInit = new byte[tailleSegment];
        
        try {
            this.reader = new FileInputStream(this.fileDescriptor);
            this.readerBuffer = new BufferedInputStream(this.reader);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtaining the next file's part to send
     * @return file's part
     */
    public byte[] getFilePart() {
        this.filePart = this.filePartInit;
        try {
            
            if(this.readerBuffer.read(filePart)==-1){
                this.isLast = true; 
            }
            this.setProgression(this.getProgression()+filePart.length);              
            //set isLast to true when last part has been loaded
        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (isLast) {
                try {
                    this.readerBuffer.close();
                    this.reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileSendingInformation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //consomme moins de mémoire que l'utilisation d'un nouveau tableau à chaque fois mais n'arrange pas le problème 
        // de saturation de la mémoire pour des fichiers énormes
        return filePart.clone();
    }

}
