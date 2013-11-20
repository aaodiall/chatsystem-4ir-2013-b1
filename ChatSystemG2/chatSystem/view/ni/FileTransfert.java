package chatSystem.view.ni;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import chatSystem.model.FileTransfertInformation;
import chatSystem.model.FileTransferts;

//SERVEUR
public class FileTransfert implements Runnable {
    
    private ServerSocket serverSocket = null;
    private Socket echoSocket = null;
    BufferedWriter writer = null;
    FileTransfertInformation fileToSend;
    
    public FileTransfert(int port, int idTransfert) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.serverSocket.accept();
            writer = new BufferedWriter(new OutputStreamWriter(echoSocket.getOutputStream()));
            this.fileToSend = FileTransferts.getInstance().getFileTransfertInformation(idTransfert);                   
        } catch(IOException e) {
            System.err.println("Could not listen on port or accept a connection from outside");
        }
    }


    public void SendFile() {
        //this.fileToSend.getFilePart()
    }
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
