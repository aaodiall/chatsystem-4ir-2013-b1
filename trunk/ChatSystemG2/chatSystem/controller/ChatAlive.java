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

    private long msBetweenHellos;
    private long msTimeToAnswer;
    private RemoteSystems rmInstance = null;
      
    /**
     * Class' constructor
     */
    public ChatAlive() {
        this.rmInstance = RemoteSystems.getInstance();
        this.msBetweenHellos = 10000; //10s
        this.msTimeToAnswer = 2000; //2s
    } 
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.msBetweenHellos - this.msTimeToAnswer);
                rmInstance.setAllMaybeOffline();
                Thread.sleep(this.msTimeToAnswer);
                this.rmInstance.removeOfflineRemoteSystem();
            } catch (InterruptedException e) {
                System.err.println("Someone interrupted ChatAlive for no reason");
            }
        }
    }   
}
