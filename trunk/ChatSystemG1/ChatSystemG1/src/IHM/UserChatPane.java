package IHM;

import javax.swing.JTabbedPane;

public class UserChatPane extends JTabbedPane {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3209546925168038332L;

	public UserChatPane() {
		// TODO Auto-generated constructor stub
		this.Init();
		

		}
	public void AddTab(String RemoteUsername){
		this.addTab(RemoteUsername, new UserChatTextPane());
	    
	}
	public void closeTab(int selectedTab){
		if(selectedTab != -1){
		this.removeTabAt(selectedTab);
		}
	}
	/**
	 * @return the selectedTab
	 */
	public int getSelectedTab() {
		return this.getSelectedIndex();
	}
	public void Init(){
		
		this.AddTab("bonjour");
		
		this.setTabLayoutPolicy(1);

		this.setVisible(true);
	}
    
    
}
