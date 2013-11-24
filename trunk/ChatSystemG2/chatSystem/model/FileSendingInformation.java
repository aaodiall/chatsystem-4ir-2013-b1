/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem.model;

import java.io.BufferedInputStream;
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
public class FileSendingInformation extends FileTransfertInformation {

    private BufferedInputStream readerBuffer;
    private FileInputStream reader;
    
    byte[] filePart;

    public FileSendingInformation(String idRemoteSystem, File fileToSend) {
        super(idRemoteSystem, fileToSend);

        this.filePart = new byte[FileTransfertInformation.tailleSegment];
        
        try {
            this.reader = new FileInputStream(this.fileDescriptor);
            this.readerBuffer = new BufferedInputStream(this.reader);
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
        try {
            if (this.readerBuffer.read(filePart) == -1) {
                this.isLast = true;
            }

            //set isLast to true when last part has been loaded
            this.sizeTransfered += filePart.length;
            if (isLast) {
                this.readerBuffer.close();
                this.reader.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filePart;
    }

}
