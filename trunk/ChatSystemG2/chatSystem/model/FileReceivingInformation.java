/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatSystem.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jules
 */
public class FileReceivingInformation extends FileTransfertInformation{
    
    private FileOutputStream writer;
    
    private final long size;

    public FileReceivingInformation(String idRemoteSystem, long size, String name) {
        super(idRemoteSystem, new File(name));
        
        this.size = size;
        
        //For now we just have a temporary file until the user choose how to name it and where to save it
        
    }
    
    /**
     * Add a new file part
     * @param filePart
     */
    public void addFilePart(byte[] filePart) {
        try {
            this.writer.write(filePart);

        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setFileDescriptor(File fileToSave){
        this.fileDescriptor = fileToSave;
        try {
            this.writer = new FileOutputStream(this.fileDescriptor);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
