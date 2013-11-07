package chatSystem.view.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class DialogWindow extends JFrame {

	/**
	 * num�ro de version
	 */
	private static final long serialVersionUID = 1L;
	
	private String contact;
	private JTextArea message;
	private JTextArea conversation;
	private JButton send;
	private JButton joinFile;
	private JProgressBar progression;
	
	public DialogWindow (String contact) {
		this.contact = contact;
		this.message = new JTextArea();
		this.conversation = new JTextArea();
		this.send = new JButton("Send");
		this.joinFile = new JButton("Join File");
		this.progression = new JProgressBar();
		InitWindow();
	}
	
	private void InitWindow() {
		this.setTitle("Dialogue Window" + this.contact);
		this.setSize(300, 400);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new  GridBagConstraints();
		
		//contraintes pour le texte conversation
		JScrollPane scrollConv = new JScrollPane(this.conversation);
		scrollConv.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
		scrollConv.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.conversation.setEditable(false);

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 6;
		c.gridheight = 20;
		c.ipady = 200;
		c.ipadx = 50;
		c.weightx = 0.0;
		panel.add(scrollConv, c);
		
		//contraintes pour la barre de progression
		c.gridx = 0;
		c.gridy = 20;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 6;
		c.gridheight = 2;
		c.ipady = 10;
		c.ipadx = 50;
		c.weightx = 0.0;
		panel.add(this.progression, c);
		
		//contraintes pour le text message
		JScrollPane scrollMess = new JScrollPane(this.conversation);
		scrollMess.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
		scrollMess.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.message.setEditable(true);
		
		c.gridx = 0;
		c.gridy = 22;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 6;
		c.gridheight = 2;
		c.ipady = 20;
		c.ipadx = 50;
		c.weightx = 0.0;
		panel.add(scrollMess, c);
		
		//contraintes pour le bouton send
		c.gridx = 2;
		c.gridy = 24;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipady = 10;
		c.ipadx = 10;
		c.weightx = 0.0;
		panel.add(this.send, c);
		
		//contraintes pour le bouton join file
		c.gridx = 5;
		c.gridy = 24;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipady = 10;
		c.ipadx = 10;
		c.weightx = 0.0;
		panel.add(this.joinFile, c);
		
		this.add(panel, BorderLayout.PAGE_START);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}