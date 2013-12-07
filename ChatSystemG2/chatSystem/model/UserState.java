package chatSystem.model;

/**
 * Represents the state a user can be on : connected/deconnected
 * or maybe offline if the system is trying to find out whether the remote system is connected or not 
 */
public enum UserState {
	CONNECTED,
	DISCONNECTED,
        MAYBEOFFLINE;
}
