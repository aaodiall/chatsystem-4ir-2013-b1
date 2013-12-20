/**
 * 
 */
package chatSystemIHMs;



/**
 * this exception is used to control local user's data transfers
 * @author joanna
 *
 */
public class TransferException extends Exception{

	private int type;
	private final String fileSize = "the file to send exceed 2Go, transfer refused";
	private final String fileNumber = "There are too many transfers in progress please retry later";
	private final String receivingsize = "The total size of the files to receive exceed 2Go, tranfer refused. Please retry later";
	private final int typeSizeTransfer = 1;
	private final int typeNumberOfFile = 2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransferException(int i){
		this.type = i;
	}
	
	String getMessageToSize(){
		return this.fileSize;
	}
	
	String getMessageToNumber(){
		return this.fileNumber;
	}
	
	String getMessageToReceive(){
		return this.receivingsize;
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
}