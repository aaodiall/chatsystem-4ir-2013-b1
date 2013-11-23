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
public class ChatAlive extends Thread{

    private long msBetweenHellos;
    private long msTimeToAnswer;
    private RemoteSystems rmInstance = null;
      
    /**
     * Class' constructor
     */
    public ChatAlive() {
        this.rmInstance = RemoteSystems.getInstance();
        this.msBetweenHellos = 60000; //1min
        this.msTimeToAnswer = 2000; //2s
    } 
    
    @Override
    public void run() {
        while (true) {
            try {
                this.sleep(this.msBetweenHellos - this.msTimeToAnswer);
                rmInstance.setAllMaybeOffline();
                this.sleep(this.msTimeToAnswer);
                this.rmInstance.removeOfflineRemoteSystem();
            } catch (InterruptedException e) {
                System.err.println("Someone interrupted ChatAlive for no reason");
            }
        }
    }   
}
