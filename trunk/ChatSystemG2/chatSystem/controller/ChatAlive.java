/*
 * Responsible for the sending of new hello messages from time to time
 * In order to inform the other remote systems the user is still connected
 * and learn if some remote systems have gone away
 */

package chatSystem.controller;

import chatSystem.model.RemoteSystems;

/**
 *
 * @author Marjorie
 */
public class ChatAlive implements Runnable{

    private static long msToWait = 1000;
    private RemoteSystems rmInstance = null;
      
    public ChatAlive() {
        this.rmInstance = RemoteSystems.getInstance();
    } 
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.currentThread().sleep(msToWait);
                
            } catch (InterruptedException e) {
                System.err.println("Someone interrupted ChatAlive for no reason");
            }
        }
    }
    
    
    
}
