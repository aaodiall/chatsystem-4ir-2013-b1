/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatSystem.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jules
 */
public class FileSendingInformation extends FileTransfertInformation{
    
    private FileInputStream reader;

    public FileSendingInformation(int idTransfert, String idRemoteSystem, File fileToSend) {
        super(idTransfert, idRemoteSystem, fileToSend);
        
        try {
            this.reader = new FileInputStream(this.fileDescriptor);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Obtaining the next file's part to send
     *
     * @return file's part
     */
    public byte[] getFilePart() {
        byte[] filePart = new byte[FileTransfertInformation.tailleSegment];
        try {
            if(this.reader.read(filePart) == -1){
                this.isLast = true;
            }
            
            //set isLast to true when last part has been loaded
        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filePart;
    }
    
}
