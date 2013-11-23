/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatSystem.model;

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

    public FileReceivingInformation(String idRemoteSystem, long size, String name) {
        super(idRemoteSystem, name);
        
        this.state = FileState.ACCEPTED;
        this.size = size;
        try {
            this.fileDescriptor.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.writer = new FileOutputStream(name);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
}
