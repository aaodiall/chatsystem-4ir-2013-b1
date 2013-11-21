package chatSystem.view.ni;

//CLIENT
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReceiver implements Runnable {

    private final String ipServer;
    private final int portServer;
    
    private Socket socketClient;
    private ObjectInputStream reader;

    public FileReceiver(String ipServer, int portServer) {
        this.ipServer = ipServer;
        this.portServer = portServer;
    }

    @Override
    public void run() {
        try {
            //Le client commence par tenter la connection
            
            try {
                this.socketClient = new Socket(this.ipServer, this.portServer);
            } catch (IOException ex) {
                Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                this.reader = new ObjectInputStream(socketClient.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Object msg = reader.readObject();
            
            System.out.println("RECU : "+msg.toString());
        } catch (IOException ex) {
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
