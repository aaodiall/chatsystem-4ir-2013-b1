/**
 * 
 */
package chatSystemIHMs;

/**
 * @author joanna
 * this exception is used to control local user's data transfers
 */
public class TransferException extends Exception{

	private int type;
	private final int typeSizeTransfer = 1;
	private final int typeNumberOfFile = 2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransferException(int i){
		this.type = i;
	}
	
	/**
	 * 
	 * @return true if the exception has been thrown because of maxSizeTranfer 
	 */
	public boolean isSizeTransfer(){
		return this.type == this.typeSizeTransfer;
	}
	
	/**
	 * 
	 * @return true if the exception has been thrown because of the number of files to send
	 */
	public boolean isNumberOfFile(){
		return this.type == this.typeNumberOfFile;
	}
	
	/**
	 * indicates to local user that an error occured because of the total size of data he is trying to send
	 */
	public void handleSizeTransfer(){
		// A IMPLEMENTER EN FONCTION DE LA GUI
		System.out.println("attemp of sending more than 2Go, file not sent");
	}
	
	/**
	 * indicates to local user that an error occured because of the number of files he is trying to send
	 */
	public void handleTooManyFiles(){
		// A IMPLEMENTER EN FONCTION DE LA GUI
		System.out.println("attemp of sending more than 6 files in the same time, file not sent");
	}
	
}