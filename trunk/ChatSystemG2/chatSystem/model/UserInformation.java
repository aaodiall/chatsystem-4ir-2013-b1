package chatSystem.model;

public class UserInformation {
	
	private String username;
	private String ip;
	private UserState state;
	
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
	
}
