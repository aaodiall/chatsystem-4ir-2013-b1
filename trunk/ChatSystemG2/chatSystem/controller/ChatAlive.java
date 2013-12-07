package chatSystem.controller;

import chatSystem.model.RemoteSystems;
import chatSystem.model.UserInformation;
import chatSystem.model.UserState;

/*
 * Responsible for the sending of new hello messages from time to time
 * In order to inform the other remote systems the user is still connected
 * and learn if some remote systems have gone away
 */
public class ChatAlive extends Thread{

    private final long msBetweenHellos;
    private final long msTimeToAnswer;
    private final UserInformation usrInfo;
    private RemoteSystems rmInstance = null;
      
    /**
     * Class' constructor
     * @param usrInfo information about the local user
     */
    public ChatAlive(UserInformation usrInfo) {
        this.rmInstance = RemoteSystems.getInstance();
        this.usrInfo = usrInfo;
        this.msBetweenHellos = 20000; //10s
        this.msTimeToAnswer = 15000; //6s
    } 
    
    /**
     * Action to be executed
     */
    @Override
    public void run() {
        while (this.usrInfo.getUserState() == UserState.CONNECTED) {
            try {
                ChatAlive.sleep(this.msBetweenHellos-this.msTimeToAnswer);
                rmInstance.setAllMaybeOffline();
                ChatAlive.sleep(this.msTimeToAnswer);
                this.rmInstance.removeOfflineRemoteSystem();
            } catch (InterruptedException e) {
                System.err.println("Someone interrupted ChatAlive for no reason");
            }
        }
    }   
}
