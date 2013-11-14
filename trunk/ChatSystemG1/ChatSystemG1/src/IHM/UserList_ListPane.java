package IHM;

import javax.swing.JList;
import javax.swing.JScrollPane;

public class UserList_ListPane extends JScrollPane {

	
	private JList UserList;
	/**
	 * @return the userList
	 */
	public JList getUserList() {
		return UserList;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5638035664349828454L;

	public UserList_ListPane() {
		// TODO Auto-generated constructor stub
		
		this.UserList = new JList();
		
		this.setViewportView(UserList);
		this.setVisible(true);
	}

	public void setUserList(JList jList) {
		// TODO Auto-generated method stub
		this.UserList = jList;
	}

}
