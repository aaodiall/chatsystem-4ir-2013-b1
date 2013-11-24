/**
 * Active class responsible for the file's transferts from this local system to
 * a given remote system
 */
package chatSystem.view.ni;

import chatSystem.model.FileSendingInformation;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import chatSystem.model.FileTransferts;
import chatSystemCommon.FilePart;
import chatSystemCommon.Message;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

//SERVEUR
public class FileTransfert implements Runnable {

    private final ChatNI chatNI;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private ObjectOutputStream writer;
    private BufferedOutputStream writerBuffer;

    private final FileSendingInformation fileToSend;

    /**
     * Class' constructor
     *
     * @param idTransfert id of the file transfert the instance has to execute
     * @param chatNI instance of chat ni which is responsible for this file
     * transferts instance
     */
    public FileTransfert(int idTransfert, ChatNI chatNI) {
        //System.out.println("Init serveur envoi de fichier");
        try {
            this.serverSocket = new ServerSocket(0); // let the OS find the right port
        } catch (IOException ex) {
            Logger.getLogger(FileTransfert.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fileToSend = (FileSendingInformation) FileTransferts.getInstance().getFileTransfertInformation(idTransfert);
        this.chatNI = chatNI;
    }

    /**
     * Determine the port the file transfert is going to be executing on
     *
     * @return port
     */
    public int getPort() {
        return this.serverSocket.getLocalPort();
    }

    /**
     * Action done by the active class
     */
    @Override
    public void run() {

        try {
            //attente de connection
            this.clientSocket = this.serverSocket.accept();

            //on est connecté, on prépare de transfert
            this.writer = new ObjectOutputStream(clientSocket.getOutputStream());
            this.writerBuffer = new BufferedOutputStream(writer);

            Message msg;
            try {
                do {
                    //on récupère le morceau de fichier a envoyer et on l'écrit dans la socket
                    msg = new FilePart(this.chatNI.getUserInfo().getUsername(), this.fileToSend.getFilePart(), this.fileToSend.isLast());//a changer mais je vais vite

                    byte[] tmp = msg.toArray();
                    this.writerBuffer.write(tmp);
                    this.writerBuffer.flush();
                } while (!this.fileToSend.isLast());

            } finally {
                //on termine
                
                this.writerBuffer.close();
                this.writer.close();
                this.clientSocket.close();
                this.serverSocket.close();
            }

            this.chatNI.fileSended(this.fileToSend.getId(), this.fileToSend.getIdRemoteSystem());

        } catch (IOException ex) {
            Logger.getLogger(FileTransfert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
