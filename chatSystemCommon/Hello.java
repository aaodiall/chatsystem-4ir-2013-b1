package chatSystemCommon;


public class Hello extends Message{
	
	private static final long serialVersionUID = 8847301895396804701L;
	
	private boolean isAck;
	
	public Hello(String username, boolean isAck) {
		super(username);
		
		this.isAck = isAck;
	}

	public boolean isAck() {
		return isAck;
	}
	

}
