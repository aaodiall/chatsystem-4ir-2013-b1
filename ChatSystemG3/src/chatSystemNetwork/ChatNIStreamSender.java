/**
 * 
 */
package chatSystemNetwork;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import chatSystemCommon.FilePart;

/**
 * This class is responsible for file sending
 * @author joanna
 *
 */
public class ChatNIStreamSender extends Thread{
	
	private ServerSocket serverSocket;
	private Socket sock;
	private ObjectOutputStream out;
	private boolean firstPart;
	
	/**
	 * Constructor of the ChatNIStreamSender
	 */
	public ChatNIStreamSender(){
		try {
			this.serverSocket = new ServerSocket(0);
			this.firstPart = true;
		} catch (IOException e) {
			System.err.println("Error : server socket not created");
			e.printStackTrace();
		}		
	}
	
	/**
	 * gets the number associated to the TCP server socket
	 * @return port used by the server
	 */
	public int getNumPort(){
		return this.serverSocket.getLocalPort();
	}
	
	/**
	 * sends a part of file
	 * @param f part
	 */
	public void sendPart(FilePart f){
		try {
			if(this.firstPart){
			 	this.out = new ObjectOutputStream(new BufferedOutputStream(this.sock.getOutputStream()));
				this.firstPart=false;
			}
			/* reset pour que l'ObjectOutputStream oublie tout ce qu'il a déjà envoyé
			 * si ce n'est pas fait on ne peut pas envoyer plusieurs fois le même tableau (même si le contenu est différent)
			 * car ces objets gardent des références en mémoire (seul le premier tableau sera réellement reçu à chaque envoi)
			 */
			this.out.reset();
			this.out.writeObject(f);
			// parce qu'on utilise un bufferedOuputStream
			this.out.flush();
			// si c'est le dernier envoi on ferme le flux et le socket
			if (f.isLast()){
				this.out.close();
				this.sock.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/*	Ce thread sert juste a accepter la connexion
	 * 	c'est le thread du controller qui lit le model et apres utilise sendPart pour envoyer le fichier
	 * 	Choix fait car :
	 * 		- seul le controller peut accéder directement au model
	 * 		- si on utilisait un buffer d'envoi	on aurait des soucis de mémoire (error java heap space) pour les gros fichiers
	 * 		- si on utilisait un buffer d'envoi on aurait la contrainte	temps d'alimentation du buffer < temps d'envoi de la partie 
	 * 		- c'était notre option la plus optimale en terme de temps d'execution
	 * */
	/**
	 * accept the connection and initialize the socket dedicated to the transfer 
	 */
	@Override
	public void run(){
		try {
			this.sock = this.serverSocket.accept();
			this.serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error : accept in StreamConnection");
			e.printStackTrace();
		}
	}
}