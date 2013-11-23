/*
 * Responsible for the sending of new hello messages from time to time
 * In order to inform the other remote systems the user is still connected
 * and learn if some remote systems have gone away
 */

package chatSystem.controller;

import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;

/**
 *
 * @author Marjorie
 */
public class ChatAlive extends Thread{

    private final long msBetweenHellos;
    private final long msTimeToAnswer;
    private final UserInformation usrInfo;
    private RemoteSystems rmInstance = null;
      
    /**
     * Class' constructor
     */
    public ChatAlive(UserInformation usrInfo) {
        this.rmInstance = RemoteSystems.getInstance();
        this.usrInfo = usrInfo;
        this.msBetweenHellos = 60000; //1min
        this.msTimeToAnswer = 2000; //2s
    } 
    
    @Override
    public void run() {
        while (true) {
            try {
                this.sleep(this.msBetweenHellos - this.msTimeToAnswer);
                rmInstance.setAllMaybeOffline();
                this.usrInfo.setUserState(UserState.CONNECTED);
                this.sleep(this.msTimeToAnswer);
                this.rmInstance.removeOfflineRemoteSystem();
            } catch (InterruptedException e) {
                System.err.println("Someone interrupted ChatAlive for no reason");
            }
        }
    }   
}