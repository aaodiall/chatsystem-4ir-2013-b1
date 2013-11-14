/**
 * 
 */
package chatSystemIHMs;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import chatSystemController.Controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

/**
 * @author alpha
 *
 */
public class InterfaceConnect extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller controller;
	private JTextField tfdUsername;
	private JButton btnConnect;
	private JLabel lblUsername;
	private JLabel lblWelcome;

	


	/**
	 * Create the application.
	 */
	public InterfaceConnect(Controller controller) {
		this.controller=controller;
		initialize();
		setResizable(false);
		this.setVisible(true);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		lblUsername = new JLabel("Enter your login");
		
		tfdUsername = new JTextField(30);
		tfdUsername.setColumns(10);
			
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);
		
		lblWelcome = new JLabel("     Welcome to ChatSystem!");
		lblWelcome.setFont(new Font("Dialog", Font.BOLD, 18));
		lblWelcome.setForeground(Color.BLACK);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(161)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUsername)
								.addComponent(tfdUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(173)
							.addComponent(btnConnect))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(71)
							.addComponent(lblWelcome, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(82, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(23)
					.addComponent(lblWelcome, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addGap(59)
					.addComponent(lblUsername)
					.addGap(12)
					.addComponent(tfdUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnConnect)
					.addContainerGap(79, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
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
		    	  
		    	
		  	  controller.performConnect(tfdUsername.getText());
		  		
		   }
		  }).start();
		
	}
	

	public String getTfdUsername() {
		return tfdUsername.getText();
	}

	public void setTfdUsername(String username) {
		this.tfdUsername.setText(username);
	}

	

}
