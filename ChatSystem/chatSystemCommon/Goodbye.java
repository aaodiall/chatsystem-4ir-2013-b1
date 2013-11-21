package chatSystemCommon;

public class Goodbye extends Message{
	
	private static final long serialVersionUID = 2328320902254163346L;

	public Goodbye(String username) {
		super(username);
	}

	@Override
	public String toString() {
		return "Goodbye [id=" + id + ", username=" + username  +"]";
	}	
}
