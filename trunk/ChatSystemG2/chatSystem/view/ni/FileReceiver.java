/**
 * Active class responsible for the reception of file sent from a given remote system
 */
package chatSystem.view.ni;

//CLIENT
import chatSystemCommon.FilePart;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReceiver implements Runnable {

    private final String ipServer;
    private final int portServer;
    private final ChatNI chatNI;
    private final int idTransfert;
    
    private Socket socketClient;
    private ObjectInputStream reader;

    /**
     * Class' constructor
     * @param idTransfert id of the file's reception the instance is responsible of
     * @param ipServer ip adress of the remote system sending the file
     * @param portServer port the remote system is going to use
     * @param chatNI instance of chat ni which is responsible for this instance of file receiver
     */
    public FileReceiver(int idTransfert, String ipServer, int portServer, ChatNI chatNI) {
        this.ipServer = ipServer;
        this.portServer = portServer;
        this.chatNI = chatNI;
        this.idTransfert = idTransfert;
    }

    /**
     * Action executed by the active class
     */
    @Override
    public void run() {
        try {
            //Le client commence par tenter la connection
            //System.out.println("Lancement Reception Fichier");
            try {
                this.socketClient = new Socket(this.ipServer, this.portServer);
                this.reader = new ObjectInputStream(socketClient.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Object msg;
            do{
                msg = reader.readObject();
                
                this.chatNI.filePartReceived(this.idTransfert, ((FilePart)msg).getFilePart(), ((FilePart)msg).isLast());
                //System.out.println("RECU : "+msg.toString());
            }while(!((FilePart)msg).isLast());
            
            System.out.println("-------------------FICHIER RECU----------------------");
        } catch (IOException ex) {
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
