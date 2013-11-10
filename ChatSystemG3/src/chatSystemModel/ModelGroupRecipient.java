/**
 * 
 */
package chatSystemModel;

import java.util.ArrayList;
import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelGroupRecipient extends Observable{

	ArrayList<String> groupRecipients;

	/**
	 * @param groupRecipients
	 */
	public ModelGroupRecipient() {
		this.groupRecipients = new ArrayList<String>();
	}
	
	public void addRecipient(String username){
		this.groupRecipients.add(username);
	}
	
	public ArrayList<String> getGroupRecipients(){
		return this.groupRecipients;
	}
}
