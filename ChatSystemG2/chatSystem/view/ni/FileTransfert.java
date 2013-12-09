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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Active class responsible for the file's transferts from this local system to
 * a given remote system
 */
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
            this.chatNI.fileTransfertError(this.fileToSend.getId());
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
            //waiting for a connection
            this.clientSocket = this.serverSocket.accept();

            //we are connected, preparing for a transfert
            this.writerBuffer = new BufferedOutputStream(clientSocket.getOutputStream());
            this.writer = new ObjectOutputStream(writerBuffer);

            Message msg;
            do {
                //allows sending big files (if we don't do that we keep the file in mem)
                this.writer.reset();
                //recover the file's part to send and write it in the socket
                msg = new FilePart(this.chatNI.getUserInfo().getUsername(), this.fileToSend.getFilePart(), this.fileToSend.isLast());

                this.writer.writeObject(msg);
                this.writerBuffer.flush();
            } while (!this.fileToSend.isLast());

            this.chatNI.fileSended(this.fileToSend.getId(), this.fileToSend.getIdRemoteSystem());

        } catch (IOException ex) {
            this.chatNI.fileTransfertError(this.fileToSend.getId());
            Logger.getLogger(FileTransfert.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            this.chatNI.fileTransfertError(this.fileToSend.getId());
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //finishing
                this.writerBuffer.flush();
                this.writer.close();
                this.writerBuffer.close();
                this.clientSocket.close();
                this.serverSocket.close();
            } catch (IOException ex) {
                this.chatNI.fileTransfertError(this.fileToSend.getId());
                Logger.getLogger(FileTransfert.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
