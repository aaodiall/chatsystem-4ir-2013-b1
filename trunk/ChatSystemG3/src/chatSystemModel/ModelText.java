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
	
	public ModelText (){}
	
	public String getTextToSend(){
		return this.text2send;
	}
	
	public String getTextReceived(){
		return this.textReceived;
	}
	
	public String getRemote(){
		return this.remote;
	}
	
	public void setTextToSend(String text){
		this.text2send = text;
	}
	
	public void setTextReceived(String text){
		this.textReceived = text;
	}
	
	public void setRemote (String user){
		this.remote = user;
	}

}

