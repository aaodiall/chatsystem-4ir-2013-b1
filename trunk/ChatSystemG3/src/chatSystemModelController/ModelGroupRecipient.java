/**
 * 
 */
package chatSystemModelController;

import java.util.ArrayList;
import java.util.Observable;

/**
 * @author alpha
 *
 */
public class ModelGroupRecipient extends Observable{

	ArrayList groupRecipients;

	/**
	 * @param groupRecipients
	 */
	public ModelGroupRecipient() {
		this.groupRecipients = new ArrayList();
	}
	
	public void AddRecipient(String username){
		this.groupRecipients.add(username);
	}
	
}
