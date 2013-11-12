package IHM;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class MenuGeneralOperation extends JMenu {

	private JMenuItem MenuItemClose ; 
	private JMenuItem MenuItemConnect ; 
	private JMenuItem MenuItemDisconnect ;
	private JSeparator MenuSeparator;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2053548648297535084L;

	public MenuGeneralOperation() {
		// TODO Auto-generated constructor stub
		this.setText("General");
		this.MenuItemClose = new JMenuItem("Close");
		this.MenuItemConnect = new JMenuItem("Connect");
		this.MenuItemDisconnect = new JMenuItem("Disconnect");
		this.MenuSeparator = new JSeparator();
		Init();
	}
	public void Init(){
		this.add(MenuItemConnect);
		this.add(MenuItemDisconnect);
		this.add(MenuSeparator);
		this.add(MenuItemClose);
		this.setVisible(true);
	}
	/**
	 * @return the menuItemClose
	 */
	public JMenuItem getMenuItemClose() {
		return MenuItemClose;
	}
	/**
	 * @return the menuItemConnect
	 */
	public JMenuItem getMenuItemConnect() {
		return MenuItemConnect;
	}
	/**
	 * @return the menuItemDisconnect
	 */
	public JMenuItem getMenuItemDisconnect() {
		return MenuItemDisconnect;
	}
}
