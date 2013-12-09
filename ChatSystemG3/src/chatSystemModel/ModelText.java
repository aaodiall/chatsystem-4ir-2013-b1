/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * This class is used to represent text message between local and remote users
 * @author alpha
 * 
 */
public class ModelText extends Observable{
	
	private String text2send;
	private String textReceived;
	private String remote;
	
	
	
	/**
	 * Constructor
	 */
	public ModelText() {
		this.text2send=new String();
		this.textReceived=new String();
		this.remote=new String();
	}

	/**
	 * 
	 * @return the current text to send
	 */
	public String getTextToSend(){
		return this.text2send;
	}
	
	
	/**
	 * 
	 * @return the current text sent for the local user
	 */
	public String getTextReceived(){
		return this.textReceived;
		
	}
	
	/**
	 * write a new text to send to a connected person 
	 * @param text
	 */
	public void setTextToSend(String text){
		this.text2send = text;
	}
	
	/**
	 * write a new received text to display to local user
	 * @param text
	 */
	public void setTextReceived(String text){
		this.textReceived = text;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * set the value of the remote user associated with the text message
	 * @param user
	 */
	public void setRemote (String user){
		this.remote = user;
	}
	
	/**
	 * 
	 * @return the user name associated to the text message
	 */
	public String getRemote(){
		return this.remote;
	}
}
