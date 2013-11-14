package chatsystemg5.network;

import chatsystemg5.common.*;
import java.util.Observable;

import java.util.Observer;

public /*abstract*/ class ChatNI implements Runnable, Observer {

    public ChatNI() {
    }
	
	/*public abstract void transfer_connection (Hello hi) ;
        public abstract void transfer_disconnection (Goodbye bye) ;
        public abstract void transfer_send (Message msg) ;
        public abstract void transfer_file (File fl) ;
        public abstract void transfer_request (Message msg) ;*/ 

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Observable o, Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
