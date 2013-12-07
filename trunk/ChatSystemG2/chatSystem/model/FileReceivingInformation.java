package chatSystem.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Information about a file that is being received by the local user
 */
public class FileReceivingInformation extends FileTransfertInformation{
    
    private FileOutputStream writer;
    private BufferedOutputStream writerBuffer;
    
    private final long size;
    private final int portServer;

    /**
     * Class' constructor
     * @param idTransfert file transfert's id
     * @param idRemoteSystem id of the remote system which is sending the file
     * @param size file's size
     * @param name file's name
     * @param portServer port which is used for the transfert
     */
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
     * @param filePart file's part which is to be added
     */
    public void addFilePart(byte[] filePart) {
        try {
            this.writerBuffer.write(filePart);
            this.writerBuffer.flush();
            this.setProgression(this.getProgression()+filePart.length); 
            
        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(this.isLast){
                try {
                    this.writerBuffer.flush();
                    this.writerBuffer.close();
                    this.writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileReceivingInformation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Setting where to save the file which is being received
     * @param fileToSave File which is going to represents the receiving file
     */
    public void setFileDescriptor(File fileToSave){
        this.fileDescriptor = fileToSave;
        try {
            this.writer = new FileOutputStream(this.fileDescriptor);
            this.writerBuffer = new BufferedOutputStream(writer);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Determine the file's size
     * @return file's size
     */
    @Override
    public long getSize(){
        return size;
    }
    
}
