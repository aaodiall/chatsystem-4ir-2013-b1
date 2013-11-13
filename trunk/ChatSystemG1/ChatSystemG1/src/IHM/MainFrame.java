package IHM;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

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
		
		InitComponents();
		
	}
	
	/**
	 * @return the menu
	 */
	public Menu getMenu() {
		return menu;
	}

	

	public void InitComponents(){
		this.menu = new Menu();
		this.UCpane = new UserChatPane();
		this.ULpane = new UserListPane();
		this.MSpane = new MessageSendPane();
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
        setMinimumSize(new java.awt.Dimension(800, 626));
        setPreferredSize(new java.awt.Dimension(800, 626));
        this.setJMenuBar(menu);
        
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints c1 = new GridBagConstraints();
        GridBagConstraints c2 = new GridBagConstraints();
        GridBagConstraints c3 = new GridBagConstraints();
        c1.weighty = 0.8; 
        c1.weightx = 1;
        c1.anchor = GridBagConstraints.NORTHWEST;
        c1.gridx = 1;
        c1.gridy = 1;
        c1.insets = new Insets(12,12,12,12);

        c1.fill = GridBagConstraints.BOTH;
        add(UCpane,c1);
        
        c1.weighty = 1; 
        c1.weightx = 1;
        c1.anchor = GridBagConstraints.NORTHEAST;
        c1.gridx = 1;
        c1.gridy = 1;
        c1.fill = GridBagConstraints.NONE;
        c1.insets = new Insets(12,-12,0,0);

        add(new TabButton(UCpane),c1);
        
        c2.weighty = 0; 
        c2.weightx = 0;
        c2.gridx = 2;
        c2.gridy = 1;
        c2.gridheight = 2;
        c2.insets = new Insets(12,12,12,12);
        c2.fill = GridBagConstraints.BOTH;
        add(ULpane,c2);
        
        c3.weighty = 0; 
        c3.weightx = 0;
        c3.gridx = 1;
        c3.gridy = 2;
        c3.gridwidth = 1;
        c3.insets = new Insets(0,12,12,0);
        c3.fill = GridBagConstraints.BOTH;
        add(MSpane,c3);
        
        setVisible(true);
	}
	private class TabButton extends JButton implements ActionListener {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1448978011543718242L;
		private UserChatPane UCP;
        public TabButton(UserChatPane UC) {
        	this.UCP =UC;
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close the Selected Tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
 
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == this){
            	this.UCP.closeTab(UCP.getSelectedTab());
            }
        }
 
        
 
        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
}
