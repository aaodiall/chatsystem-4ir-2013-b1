package chatSystem.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *  Scinder cette classe en deux -> une qui permet d'écrire un fichier recus et une qui permet de lire un fichier a envoyer ? 
 */
public class FileTransfertInformation extends Model {

    private static int idCpt;
    private static final int tailleSegment = 1024;
    
    private final int idTransfert;
    private final String idRemoteSystem;
    private FileState state;
    private final String name;
    private final long taille;
    private String path;
    
    private final int tailleRecup;
    private boolean isLast;
    
    private File fileDescriptor;
    
    private FileInputStream reader;
    private FileOutputStream writer;

    /**
     * Class' constructor to send the file
     *
     * @param idRemoteSystem id of the sending remote system
     * @param name file's name
     */
    public FileTransfertInformation(String idRemoteSystem, String name) {
        
        this.idRemoteSystem = idRemoteSystem;
        this.name = name;
        this.idTransfert = FileTransfertInformation.idCpt++;
        this.state = FileState.WAITANSWER;
        this.tailleRecup = 0;
        this.path = null;
        this.isLast = false;
        
        this.fileDescriptor = new File(name);
        
        this.taille = this.fileDescriptor.length();
        try {
            this.reader = new FileInputStream(this.fileDescriptor);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Class' constructor to receive the file
     *
     * @param taille file's size
     * @param idRemoteSystem id of the sending remote system
     * @param name file's name
     */
    public FileTransfertInformation(String idRemoteSystem, long taille, String name) {
        
        this.idRemoteSystem = idRemoteSystem;
        this.idTransfert = FileTransfertInformation.idCpt++;
        
        this.name = name;
        this.taille = taille;
        
        this.state = FileState.ACCEPTED;
        this.tailleRecup = 0;
        this.path = null; //in name or to define
        this.isLast = false;
        
        System.out.println(name);
        this.fileDescriptor = new File(name);
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
     * Obtaining the file's state
     *
     * @return file's state
     */
    public FileState getState() {
        return this.state;
    }

    /**
     * Obtaining where the system is in the file's tranfer
     *
     * @return transfer's progression
     */
    public int getProgression() {
        return 0;
    }

    /**
     * Say if the last part loaded is the last part of the file
     * @return isLast attribute
     */
    public boolean isLast() {
        return this.isLast;
    }
    
    public boolean setIsLast(boolean isLast) {
        return this.isLast = isLast;
    }

    /**
     * Obtaining the next file's part to send
     *
     * @return file's part
     */
    public byte[] getFilePart() {
        byte[] filePart = new byte[1024];
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

    /**
     * Obtaining the file's path in the system
     *
     * @return file's path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Setting the file's path in the system
     *
     * @param path new file's path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Setting the sending state of the file
     *
     * @param state new file's state
     */
    public void setState(FileState state) {
        this.state = state;
    }

    /**
     * Add a new file part
     */
    public void addFilePart(byte[] filePart) {
        try {
            this.writer.write(filePart);

        } catch (IOException ex) {
            Logger.getLogger(FileTransfertInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getId(){
        return this.idTransfert;
    }
    
    public String getName(){
        return this.name;
    }
    
    public long getSize(){
        return this.taille;
    }
    
    public String getIdRemoteSystem(){
        return this.idRemoteSystem;
    }
}
