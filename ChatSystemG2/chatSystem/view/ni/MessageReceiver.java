package chatSystem.view.ni;

import java.net.*;

public class MessageReceiver {
    
    private DatagramSocket serverSocket;
    private byte[] messageReceived;
    
    
    public void MessageReceiver(int serverPort, int tailleMax) {
        try {
            this.serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException exc) {
            System.out.println("Probleme à la création du socket server");
        }
    }
    
    public void ReceiveHello() {
        
    }

}


/*class ServeurEcho 
{ 
  final static int port = 8532; 
  final static int taille = 1024; 
  final static byte buffer[] = new byte[taille];

  public static void main(String argv[]) throws Exception 
    { 
      DatagramSocket socket = new DatagramSocket(port); 
      while(true) 
      { 
        DatagramPacket data = new DatagramPacket(buffer,buffer.length); 
        socket.receive(data); 
        System.out.println(data.getAddress()); 
        socket.send(data); 
      } 
    } 
}*/