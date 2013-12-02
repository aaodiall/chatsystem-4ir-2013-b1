/**
 * Information about a file that is being sent by the local user
 */

package chatSystem.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSendingInformation extends FileTransfertInformation {

    private BufferedInputStream readerBuffer;
    private FileInputStream reader;
    ByteArrayOutputStream writerArray;

    /**
     * Class' constructor
     * @param idRemoteSystem id of the remote system the file is being sent to
     * @param fileToSend file which is to be sent 
     */
    public FileSendingInformation(String idRemoteSystem, File fileToSend) {
        super(idRemoteSystem, fileToSend);
        this.writerArray = new ByteArrayOutputStream();
        
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
        this.writerArray.reset();
        int cpt = 1,tmp = 0;
        try {
            
            //get the byte
            do{
                tmp = this.readerBuffer.read();
                if(tmp == -1){
                    this.isLast = true; 
                }else{
                    this.writerArray.write(tmp);
                    cpt++;
                }
            }while(cpt<tailleSegment && tmp!=-1);
            
            this.setProgression(this.getProgression()+cpt);              
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
        return writerArray.toByteArray();
    }

}
