package chatSystemCommon;

import java.util.Arrays;


public class FilePart extends Message{
	
	/** 
	 * id for serialization
	 */
	private static final long serialVersionUID = -3565202401661564908L;
	
	//Array of byte which contains the file part sent/received
	private byte[] filePart;
	private boolean isLast;
	
	public FilePart(String username, byte[] filePart, boolean isLast) {
		super(username);
		this.filePart = filePart;
		this.isLast = isLast;	
	}

	public byte[] getFilePart() {
		return filePart;
	}
	
	public void setFilePart(byte[] filePart) {
		this.filePart = filePart;
	}

	public boolean isLast() {
		return isLast;
	}
	
	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}

	@Override
	public String toString() {
		return "Hello [id=" + id + ", username=" + username  +", filePart=" + Arrays.toString(filePart) + ", isLast=" + isLast + "]";
	}
}