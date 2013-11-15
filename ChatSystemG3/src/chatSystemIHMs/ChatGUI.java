/**
 * 
 */
package chatSystemIHMs;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DefaultListModel;

import chatSystemController.Controller;
import chatSystemModel.ModelListUsers;
import chatSystemModel.ModelStates;
import chatSystemModel.ModelUsername;

/**
 * @author alpha
 *
 */
public class ChatGUI extends View implements Observer{
	private InterfaceConnect wConnect;
	private InterfaceCommunicate wCommunicate;
	
	//private ToUser toUser;
	//private FromUser fromUser;
	

	public InterfaceConnect getwConnect() {
		return wConnect;
	}
	public InterfaceCommunicate getwCommunicate() {
		return wCommunicate;
	}
	/**
	 * @param wConnect
	 * @param wCommunicate
	 */
	public ChatGUI(Controller controller) {
		this.wConnect = new InterfaceConnect(controller);;
		this.wCommunicate = new InterfaceCommunicate(controller,wConnect.getTfdUsername());
	
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
		if(arg0.getClass()==ModelListUsers.class){
			wCommunicate.setUsers(( (HashMap<?, ?>)arg1 ).keySet().toArray()) ;
		}
		if(arg0.getClass().equals(ModelUsername.class)){
			wCommunicate.setLblUsername((String)arg1);
		}
		//ainsi de suite
	}
/*public static void main(String[] args) {
		
		InterfaceConnect wConnect=new InterfaceConnect();
		wConnect.setVisible(true);
		
	}
*/
	
	

}
