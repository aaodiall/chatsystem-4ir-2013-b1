package IHM;

import java.util.ArrayList;

import javax.swing.JTabbedPane;

public class UserChatPane extends JTabbedPane {
	

	private ArrayList<UserChatTextPane> TextAreas;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3209546925168038332L;

	public UserChatPane() {
		// TODO Auto-generated constructor stub
		this.Init();
		

		}
	public void AddTab(String RemoteUsername){
		UserChatTextPane uctp = new UserChatTextPane();
		TextAreas.add(uctp);
		this.addTab(RemoteUsername, uctp);
	    
	}
	public void closeTab(int selectedTab){
		if(selectedTab != -1){
			this.TextAreas.remove(this.getComponentAt(selectedTab));
			this.removeTabAt(selectedTab);
		}
	}
	/**
	 * @return the selectedTab
	 */
	public int getSelectedTab() {
		return this.getSelectedIndex();
	}
	public ArrayList<UserChatTextPane> getTextAreas(){
		return this.TextAreas;
	}
	public void Init(){
		this.TextAreas = new ArrayList<UserChatTextPane>();
		this.AddTab("bonjour");
		this.TextAreas.get(0).getTextArea().setText("Pour commencer veuillez-vous connecter\n");
		this.setTabLayoutPolicy(1);

		this.setVisible(true);
	}
    
    
}
