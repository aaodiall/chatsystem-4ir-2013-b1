package IHM;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class UserListPane extends JPanel{
	private UserList_ListPane ULLpane;
	private JLabel ULlabel;
	private JButton MSSend;
	private JButton ULChooseUser;
	/**
	 * 
	 */
	private static final long serialVersionUID = 20819910049411411L;

	public UserListPane() {
		// TODO Auto-generated constructor stub
		this.ULlabel = new JLabel("Other Users");
		this.ULLpane = new UserList_ListPane();
		 MSSend = new JButton("Envoyer");
	        MSSend.setPreferredSize(new Dimension(100,60));
	        ULChooseUser = new JButton("Choisir User");
	        ULChooseUser.setPreferredSize(new Dimension(100,60));
		 this.setLayout(new GridBagLayout());
	        
	     GridBagConstraints c1 = new GridBagConstraints();
	     c1.weightx = 0; 
	     c1.weighty = 0;
	     c1.gridx = 1;
	     c1.gridy = 1;        

	     c1.fill = GridBagConstraints.BOTH;
        add(ULlabel,c1);
        c1.weightx = 1; 
	     c1.weighty = 1.5;
        c1.gridx = 1;
        c1.gridy = 2;
        c1.fill = GridBagConstraints.BOTH;
        add(ULLpane,c1);
        c1.weightx = 0;
        c1.weighty = 0;
        c1.gridx = 1;
        c1.gridy = 3;
        
        c1.insets = new Insets(12,0,12,0);
        c1.fill = GridBagConstraints.BOTH;
        add(ULChooseUser,c1);
        c1.weightx = 0;
        c1.weighty = 0;
        c1.gridx = 1;
        c1.gridy = 4;
        
        c1.insets = new Insets(0,0,0,0);
        c1.fill = GridBagConstraints.BOTH;
        add(MSSend,c1);
		this.setVisible(true);
		
	}

	/**
	 * @return the uLLpane
	 */
	public UserList_ListPane getULLpane() {
		return ULLpane;
	}

}
