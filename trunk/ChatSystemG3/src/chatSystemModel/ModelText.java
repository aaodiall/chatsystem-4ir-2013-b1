/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelText extends Observable{
	
	private String text2send;
	private String textReceived;
	private String remote;
	
	
	
	/**
	 * 
	 */
	public ModelText() {
		this.text2send=new String();
		this.textReceived=new String();
		this.remote=new String();
	}

	public String getTextToSend(){
		return this.text2send;
	}
	
	public String getTextReceived(){
		return this.textReceived;
		
	}
	
	public void setTextToSend(String text){
		this.text2send = text;
		
	}
	
	public void setTextReceived(String text){
		this.textReceived = text;
		setChanged();
		notifyObservers();
	}
	
	public void setRemote (String user){
		this.remote = user;
	}
	public String getRemote(){
		return this.remote;
	}
}
