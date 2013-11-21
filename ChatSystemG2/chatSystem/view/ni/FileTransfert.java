package chatSystem.view.ni;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import chatSystem.model.FileTransfertInformation;
import chatSystem.model.FileTransferts;
import chatSystemCommon.FilePart;
import chatSystemCommon.Message;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

//SERVEUR
public class FileTransfert implements Runnable {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ChatNI chatNI;

    ObjectOutputStream writer; 
    FileTransfertInformation fileToSend;

    public FileTransfert(int port, int idTransfert, ChatNI chatNI) {
        System.out.println("Init serveur envoi de fichier");
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(FileTransfert.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fileToSend = FileTransferts.getInstance().getFileTransfertInformation(idTransfert);
        this.chatNI = chatNI;
    }

    public void SendFile() {
        //this.fileToSend.getFilePart()
    }

    @Override
    public void run() {
        do {
            try {
                System.out.println("Démarrage écoute Serveur envoi de fichier");
                this.clientSocket = this.serverSocket.accept();
                System.out.println("Démarrage envoi de fichier");
                this.writer = new ObjectOutputStream(clientSocket.getOutputStream());

                Message msg = new FilePart(this.chatNI.getUserInfo().getUsername(), this.fileToSend.getFilePart(), this.fileToSend.isLast());//a changer mais je vais vite
                this.writer.writeObject(msg);
                
            } catch (IOException ex) {
                Logger.getLogger(FileTransfert.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!this.fileToSend.isLast());

    }

}
