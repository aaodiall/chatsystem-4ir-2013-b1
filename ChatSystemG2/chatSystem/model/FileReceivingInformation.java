/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatSystem.model;

import java.io.BufferedOutputStream;
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
    private BufferedOutputStream writerBuffer;
    
    private final long size;
    private final int portServer;

    public FileReceivingInformation(int idTransfert, String idRemoteSystem, long size, String name, int portServer) {
        super(idTransfert, idRemoteSystem, new File(name));
        
        this.size = size;
        this.portServer = portServer;
        //For now we just have a temporary file until the user choose how to name it and where to save it
        
    }
    
    public int getPortServer(){
        return this.portServer;
    }
    
    /**
     * Add a new file part
     * @param filePart
     */
    public void addFilePart(byte[] filePart) {
        try {
            this.writerBuffer.write(filePart);
            this.setProgression(this.getProgression()+filePart.length); 
            
            if(this.isLast){
                this.writerBuffer.flush();
                this.writerBuffer.close();
                this.writer.close();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setFileDescriptor(File fileToSave){
        this.fileDescriptor = fileToSave;
        try {
            this.writer = new FileOutputStream(this.fileDescriptor);
            this.writerBuffer = new BufferedOutputStream(writer);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public long getSize(){
        return size;
    }
    
}
