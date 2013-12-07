
package chatSystem.view.ni;

import chatSystem.model.FileReceivingInformation;
import chatSystemCommon.FilePart;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Active class responsible for the reception of file sent from a given remote
 * system
 * CLIENT
 */
public class FileReceiver implements Runnable {

    private final String ipServer;
    private final int portServer;
    private final ChatNI chatNI;
    private final FileReceivingInformation fileToReceive;

    private Socket socketClient;
    private ObjectInputStream reader;
    private BufferedInputStream readerBuffer;

    /**
     * Class' constructor
     * @param fileToReceive
     * @param ipServer ip adress of the remote system sending the file
     * @param portServer port the remote system is going to use
     * @param chatNI instance of chat ni which is responsible for this instance
     * of file receiver
     */
    public FileReceiver(FileReceivingInformation fileToReceive, String ipServer, int portServer, ChatNI chatNI) {
        this.ipServer = ipServer;
        this.portServer = portServer;
        this.chatNI = chatNI;
        this.fileToReceive = fileToReceive;
    }

    /**
     * Action executed by the active class
     */
    @Override
    public void run() {
        try {
            //The client starts by trying to connect himself
            this.socketClient = new Socket(this.ipServer, this.portServer);
            this.readerBuffer = new BufferedInputStream(socketClient.getInputStream());
            this.reader = new ObjectInputStream(this.readerBuffer);
            
            Object msg;
            do {
                msg = reader.readObject();
                if(((FilePart) msg).isLast()){
                     this.fileToReceive.setIsLast(true);
                }
                this.chatNI.filePartReceived(this.fileToReceive.getId(), ((FilePart) msg).getFilePart(), ((FilePart) msg).isLast());

            } while (!((FilePart) msg).isLast());

        
        } catch (UnknownHostException ex) {
            this.chatNI.fileTransfertError(this.fileToReceive.getId());
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.chatNI.fileTransfertError(this.fileToReceive.getId());
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            this.chatNI.fileTransfertError(this.fileToReceive.getId());
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            this.chatNI.fileTransfertError(this.fileToReceive.getId());
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.reader.close();
                this.readerBuffer.close();
                this.socketClient.close();
            } catch (IOException ex) {
                this.chatNI.fileTransfertError(this.fileToReceive.getId());
                Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
