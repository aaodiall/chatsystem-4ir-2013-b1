package chatSystem.model;

public class UserInformation extends Model{
	
	private String username;
	private String ip;
	private UserState state;
	
        public UserInformation(String username, String ip) {
            this.username = username;
            this.ip = ip;
            this.state = UserState.CONNECTED;
        }
         
        public UserInformation() {}
        
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public UserState getState() {
		return this.state;
	}

	public void setState(UserState state) {
		this.state = state;
	}
	
	public String getIP() {
		return this.ip;
	}
	
        @Override
        public boolean equals(Object obj) {
           if (obj == this)
               return true;
           if (obj instanceof UserInformation) {
               UserInformation aux = (UserInformation) obj;
               return (aux.getUsername() == this.username) && (aux.getIP() == this.ip)
                       && (aux.getState() == this.state);
           }
           return false;
        }
}
