/**
 * 
 */
package chatSystemIHMs;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import runChat.ChatSystem;
import chatSystemController.Controller;
import chatSystemModel.ModelUsername;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * @author alpha
 *
 */
public class InterfaceCommunicate extends JFrame implements ActionListener{
	
	private Controller controller;
	private DefaultListModel users;
	private JButton btnJoinFile;
	private JTextArea tAreaHistoryCom;
	private JButton btnSendFile;
	private JButton btnSendMessage;
	private JTextArea tAreaMessageText;
	private JList listUsers;
	private JLabel lblUsername;
	private JButton btnDeconnection;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the application.
	 */
	public InterfaceCommunicate(Controller controller) {
		this.controller=controller;
		setResizable(false);
		initialize();
		
		
		//this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		this.setBounds(100, 5, 1000, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(621, 25, 365, 501);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		btnDeconnection = new JButton("Deconnection");
		btnDeconnection.setBounds(222, 12, 131, 25);
		panel.add(btnDeconnection);
		btnDeconnection.addActionListener(this);
		
		lblUsername = new JLabel(ChatSystem.getModelUsername().getUsername());
		lblUsername.setBounds(35, 17, 72, 15);
		panel.add(lblUsername);
		
		//test Jlist apres on utilisera un get sur le modeleusers
		users = new DefaultListModel();
		users.addElement("alpha");
		users.addElement("jojo");
				
		listUsers = new JList(users);
		listUsers.setBounds(12, 56, 340, 420);
		listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listUsers.setLayoutOrientation(JList.VERTICAL);
		panel.add(listUsers);
	
		tAreaMessageText = new JTextArea();
		tAreaMessageText.setBounds(12, 541, 793, 115);
		getContentPane().add(tAreaMessageText);
		
		btnSendMessage = new JButton("Send Message");
		btnSendMessage.setBounds(848, 554, 138, 25);
		getContentPane().add(btnSendMessage);
		
		btnSendFile = new JButton("Send File");
		btnSendFile.setBounds(848, 591, 138, 25);
		getContentPane().add(btnSendFile);
		
		tAreaHistoryCom = new JTextArea();
		tAreaHistoryCom.setBounds(12, 50, 575, 472);
		getContentPane().add(tAreaHistoryCom);
		
		btnJoinFile = new JButton("Join File");
		btnJoinFile.setBounds(848, 634, 138, 25);
		getContentPane().add(btnJoinFile);
	}

	



	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
		      public void run() {
		    	 
		  		//connect();
		    	
		  		controller.performDisconnect();
		      }
		  }).start();
		
		
	}
}
