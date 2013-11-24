/**
 * 
 */
package chatSystemIHMs;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
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
	private CommandLine cmd;
	private int mode; // 1 pour graphique, 0 pour cmd
	
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
		Scanner sc = new Scanner(System.in);
		System.out.println("Choose the mode : 1 for graphical , 0 for command line");
		int a = sc.nextInt();
		if (a == 1){
			this.mode = 1;
			this.wConnect = new InterfaceConnect(controller);;
			this.wCommunicate = new InterfaceCommunicate(controller,wConnect.getTfdUsername());
		}else{
			this.mode = 0;
			this.cmd = new CommandLine(controller);
			this.cmd.initConnection();
		}	
	}
	public int getMode(){
		return this.mode;
	}
	
	
	public void cmdUpdate(Observable arg0, Object arg1){
		if(arg0.getClass()==ModelListUsers.class){
			this.cmd.setUsers((String[])((HashMap<?,?>)arg1).keySet().toArray());
		}else if(arg0.getClass().equals(ModelUsername.class)){
			this.cmd.setUsername((String)arg1);
		}
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
