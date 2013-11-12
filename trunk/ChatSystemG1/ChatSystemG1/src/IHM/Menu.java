package IHM;

import javax.swing.JMenuBar;

public class Menu extends JMenuBar{
	
	private MenuGeneralOperation MGO;
	private MenuOtherOperation MOO;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1684312527014957045L;

	public Menu() {
		// TODO Auto-generated constructor stub
		
		this.MGO = new MenuGeneralOperation();
		this.MOO = new MenuOtherOperation();
		this.Init();
	}
	public void Init(){
		this.add(MGO);
		this.add(MOO);
		this.setVisible(true);
	}
	/**
	 * @return the mGO
	 */
	public MenuGeneralOperation getMGO() {
		return MGO;
	}
	/**
	 * @return the mOO
	 */
	public MenuOtherOperation getMOO() {
		return MOO;
	}
	
}
