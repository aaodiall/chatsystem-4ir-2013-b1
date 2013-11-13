package IHM;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class MessageSendPane extends JPanel{

	private JLabel MSLabel;
	
	private MessageTextPane MTpane;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1846108878367695819L;

	public MessageSendPane() {
		
        MSLabel = new JLabel("Envoi : ");
       
        MTpane = new MessageTextPane();
        
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints c1 = new GridBagConstraints();
        c1.weightx = 1;
        c1.weighty = 1; 
        
        c1.anchor = GridBagConstraints.NORTHWEST;
        c1.gridx = 1;
        c1.gridy = 2;
        c1.insets = new Insets(0,0,0,12);
        c1.fill = GridBagConstraints.BOTH;
        add(MTpane,c1);
        
        c1.weightx = 0;
        c1.weighty = 0;
        c1.anchor = GridBagConstraints.NORTHWEST;
        c1.gridx = 1;
        c1.gridy = 1;
        c1.insets = new Insets(0,0,0,0);
        c1.fill = GridBagConstraints.NONE;

        add(MSLabel,c1);
        
       
	        
	       
	        
		this.setVisible(true);
	}

}
