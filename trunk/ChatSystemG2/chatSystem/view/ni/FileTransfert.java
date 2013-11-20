package chatSystem.view.ni;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import chatSystem.model.FileTransfertInformation;
import chatSystem.model.FileTransferts;
import chatSystemCommon.FilePart;
import chatSystemCommon.Message;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

//SERVEUR
public class FileTransfert implements Runnable {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    OutputStream writer; // buffered or not buffered that is the question !
    FileTransfertInformation fileToSend;

    public FileTransfert(int port, int idTransfert) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(FileTransfert.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fileToSend = FileTransferts.getInstance().getFileTransfertInformation(idTransfert);
    }

    public void SendFile() {
        //this.fileToSend.getFilePart()
    }

    @Override
    public void run() {
        do {
            try {
                this.clientSocket = this.serverSocket.accept();
                this.writer = clientSocket.getOutputStream();


                Message msg = new FilePart("username", this.fileToSend.getFilePart(), this.fileToSend.isLast());//a changer mais je vais vite
                this.writer.write(msg.toArray());
                this.writer.flush(); // utile ?
                
            } catch (IOException ex) {
                Logger.getLogger(FileTransfert.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!this.fileToSend.isLast());

    }

}
