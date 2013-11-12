package IHM;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	
	private UserChatPane UCpane;
	private UserListPane ULpane;
	private MessageSendPane MSpane;
	private Menu menu;
	/**
	 * 
	 */
	private static final long serialVersionUID = 626635759873380692L;

	public MainFrame() {
		// TODO Auto-generated constructor stub
		this.menu = new Menu();
		InitComponents();
		this.setSize(800, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * @return the menu
	 */
	public Menu getMenu() {
		return menu;
	}

	

	public void InitComponents(){
		this.add(menu);
	}
}
