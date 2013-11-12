/**
 * 
 */
package chatSystemIHMs;

import java.util.Observable;
import java.util.Observer;

import chatSystemController.Controller;
import chatSystemModel.ModelListUsers;

/**
 * @author alpha
 *
 */
public class ChatGui extends View implements Observer{
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
	public ChatGui(Controller controler) {
		this.wConnect = new InterfaceConnect(controler);;
		this.wCommunicate = new InterfaceCommunicate(controler);
	
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
		if(arg0.getClass()==ModelListUsers.class){
			System.out.println("");
		}
		//ainsi de suite
	}
/*public static void main(String[] args) {
		
		InterfaceConnect wConnect=new InterfaceConnect();
		wConnect.setVisible(true);
		
	}
*/
	
	

}