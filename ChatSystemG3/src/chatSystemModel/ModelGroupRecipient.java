/**
 * 
 */
package chatSystemModel;

import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author alpha
 *
 */
public class ModelGroupRecipient extends Observable{

	BlockingQueue<String> groupRecipients;

	/**
	 * @param groupRecipients
	 */
	public ModelGroupRecipient() {
		this.groupRecipients = new ArrayBlockingQueue<String>(20);
	}
	
	public void addRecipient(String username){
		this.groupRecipients.add(username);
	}
	public void removeRecipient(String username){
		this.groupRecipients.remove(username);
	}
	
	public BlockingQueue<String> getGroupRecipients(){
		return this.groupRecipients;
	}
}
