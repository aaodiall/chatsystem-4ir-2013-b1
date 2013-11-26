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
    
    private final int tailleDernierTableau;
    private final long nbTableau;
    private long cptTableau;

    public FileSendingInformation(String idRemoteSystem, File fileToSend) {
        super(idRemoteSystem, fileToSend);
        
        tailleDernierTableau = (int) (this.fileDescriptor.length()%tailleSegment);
        System.out.println(tailleDernierTableau);
        nbTableau = this.fileDescriptor.length()/tailleSegment;
        cptTableau = 0;
        
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
        byte [] filePart = new byte[tailleSegment];
        //byte[] filePart = null;
        try {
            /*if(cptTableau < nbTableau){
                filePart = new byte[tailleSegment];
                this.readerBuffer.read(filePart);
                cptTableau++;
                System.out.println("nbTableau : "+nbTableau+"          cptTableau : "+cptTableau);
            }else{
                filePart = new byte[tailleSegment];
                this.readerBuffer.read(filePart);
                this.isLast = true; 
            }*/
            
            if(this.readerBuffer.read(filePart)==-1){
                this.isLast = true; 
            }
            
            cptTableau++;
            System.out.println("nbTableau : "+nbTableau+"          cptTableau : "+cptTableau);
                
                      
            //set isLast to true when last part has been loaded
        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.setProgression(this.getProgression()+filePart.length); 
            if (isLast) {
                try {
                    this.readerBuffer.close();
                    this.reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileSendingInformation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return filePart;
    }

}
