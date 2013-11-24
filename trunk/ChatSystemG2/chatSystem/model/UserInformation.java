/**
 * Information the system has about a user
 */

package chatSystem.model;

public class UserInformation extends Model{
	
	private String username;
	private final String ip;
	protected UserState state;
	
        /**
         * Class' constructor, the user created is still unknown
         * @param ip ip adress, only available piece of information
         */
        public UserInformation(String ip) {
            this.ip = ip;
            this.username = null;
            this.state = UserState.DISCONNECTED;
        }
        
        /**
         * Class' constructor, the system just heard about the user
         * @param username username chosen by the user
         * @param ip user' ip adress
         */
         public UserInformation(String username, String ip) {
            this.ip = ip;
            this.username = username;
            this.state = UserState.CONNECTED;
        }
        
         /**
          * Recover the user' username
          * @return username
          */
	public String getUsername() {
		return this.username;
	}
	
        /**
         * Modify the user's username
         * @param username new username
         */
	public void setUsername(String username) {
                System.out.println("Entering setUsername");
		this.username = username;
                this.setChanged();
                this.notifyObservers(this.username);
                this.clearChanged();
	}
	
        /**
         * Recover the user's state (connected/disconnected/maybeoffline)
         * @return user's state
         */
	public final UserState getUserState() {
		return this.state;
	}

        /**
         * Update the user's state (connected/disconnected/maybeoffline)
         * @param state new state
         */
	public void setUserState(UserState state) {
                System.out.println("Entering setState, the new state is : "+state.toString());
		this.state = state;
                this.setChanged();
                this.notifyObservers(this.state);
                this.clearChanged();
	}
	
        /**
         * Recover the user's ip adress
         * @return user's ip adress
         */
	public String getIP() {
		return this.ip;
	}
	
        /**
         * Evaluate whether the objet given as argument is equal to this UserInformation's instance
         * @param obj objet to compare
         * @return result of the comparison
         */
        @Override
        public boolean equals(Object obj) {
           if (obj == this)
               return true;
           if (obj instanceof UserInformation) {
               UserInformation aux = (UserInformation) obj;
               return (aux.getUsername().equals(this.username)) && (aux.getIP().equals(this.ip))
                       && (aux.getUserState() == this.state);
           }
           return false;
        }
}
