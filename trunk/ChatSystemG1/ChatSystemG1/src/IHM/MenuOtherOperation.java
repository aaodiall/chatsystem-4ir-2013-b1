package IHM;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class MenuOtherOperation extends JMenu{
	private JMenuItem MenuItemSendFile ; 
	private JMenuItem MenuItemSelectFont ; 
	private JMenuItem MenuItemAbout ;
	private JSeparator MenuSeparator;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7171541140884127414L;

	public MenuOtherOperation() {
		// TODO Auto-generated constructor stub
		this.setText("Other");
				this.MenuItemSelectFont = new JMenuItem("Font Selector");
				this.MenuItemSendFile = new JMenuItem("Send File");
				this.MenuItemAbout = new JMenuItem("About");
				this.MenuSeparator = new JSeparator();
				Init();
			}
			public void Init(){
				this.add(MenuItemSendFile);
				this.add(MenuItemSelectFont);
				this.add(MenuSeparator);
				this.add(MenuItemAbout);
				this.setVisible(true);
			}
			/**
			 * @return the menuItemSendFile
			 */
			public JMenuItem getMenuItemSendFile() {
				return MenuItemSendFile;
			}
			/**
			 * @return the menuItemSelectFont
			 */
			public JMenuItem getMenuItemSelectFont() {
				return MenuItemSelectFont;
			}
			/**
			 * @return the menuItemAbout
			 */
			public JMenuItem getMenuItemAbout() {
				return MenuItemAbout;
			}
}
