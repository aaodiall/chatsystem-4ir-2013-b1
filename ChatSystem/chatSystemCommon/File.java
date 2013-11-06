package chatSystemCommon;


public class File extends Message{
	
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -3565202401661564908L;
	
	//Array of byte which contains the file part sent/received
	private byte[] filePart;
	private boolean isLast;
	
	public File(String username, byte[] filePart, boolean isLast) {
		super(username);
		
		this.filePart = filePart;
		this.isLast = isLast;
		
	}

	public byte[] getFilePart() {
		return filePart;
	}

	public boolean isLast() {
		return isLast;
	}
	

}
