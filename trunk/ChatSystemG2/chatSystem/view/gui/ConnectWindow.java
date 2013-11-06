package chatSystem.view.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;


public class ConnectWindow extends JFrame {

	/**
	 * numéro de série 
	 */
	private static final long serialVersionUID = 1L;

	private JButton connectButton;
	private JLabel message;
	private JTextField username;
	private JLabel usernameL;

	public ConnectWindow(ChatGUI cg) {

		this.message = new JLabel("Bienvenu sur le chat System");
		this.usernameL = new JLabel("username  ");
		this.username = new JTextField(30);
		this.connectButton = new JButton("Connect");

		this.connectButton.addActionListener(cg);

		this.InitWindow();
	}

	public void InitWindow() {

		this.setTitle("Connect Window");
		this.setSize(300, 400);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new  GridBagConstraints();

		//initialisation des contraintes pour le label message
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.ipady = 50;
		c.ipadx = 100;
		c.weightx = 0.0;
		panel.add(this.message, c);

		//initialisation des contraintes pour le label usernameL
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 20;
		c.ipadx = 20;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(this.usernameL, c);

		//initialisation des contraintes pour le textField username
		c.gridx = 1;
		c.gridy = 1;
		c.ipady = 10;
		c.ipadx = 40;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(this.username, c);

		//initialisation des contraintes pour le bouton connect
		c.gridx = 1;
		c.gridy = 2;
		c.ipady = 10;
		c.ipadx = 10;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(connectButton,c);

		this.add(panel, BorderLayout.PAGE_START);
		this.setVisible(true);
	}
}

