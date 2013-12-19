/**
 * 
 */
package chatSystemNetwork;

/**
 * @author joanna
 *
 */
public class NetworkException extends Exception{

	private String message;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NetworkException(String errorMessage){
		this.message = errorMessage;
	}
	
	public void displayException(){
		System.err.println("Network Exception : "+ message);
		System.err.println("Please check your Internet connection and restart the application");
	}
}
