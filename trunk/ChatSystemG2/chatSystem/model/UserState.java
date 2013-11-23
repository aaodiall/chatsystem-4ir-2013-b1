/**
 * Represents the state a user can be on : connected/deconnected
 * or maybe offline if the system is trying to find out whether the remote system is connected or not 
 */

package chatSystem.model;

public enum UserState {
	CONNECTED,
	DISCONNECTED,
        MAYBEOFFLINE;
}
