/**
 * 
 */
package chatSystemIHMs;

import javax.swing.JFrame;
import javax.swing.JButton;

import chatSystemController.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

/**
 * @author alpha
 *
 */
public class InterfaceCommunicate extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller controller;
	private Vector<String> users;
	private JButton btnJoinFile;
	private JTextArea tAreaHistoryCom;
	private JButton btnSendFile;
	private JButton btnSendMessage;
	private JTextArea tAreaMessageText;
	private JList listUsers;
	private JLabel lblUsername;
	


	private JButton btnDeconnection;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the application.
	 */
	public InterfaceCommunicate(Controller controller , String username){
		this.controller=controller;
		setResizable(false);
		initialize(username);
		
		
		//this.setVisible(true);
	}
	
	public String getLblUsername() {
		return lblUsername.getText();
	}

	public void setLblUsername(String username) {
		this.lblUsername.setText(username);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String username) {
		
		
		
		JTabbedPane tab = new JTabbedPane();
		tab.setSize(40, 20);
		tab.add("tab",new JPanel() );
		
		this.setBounds(100, 5, 1000, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(tab);
		
		panel = new JPanel();
		panel.setBounds(621, 25, 365, 501);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		btnDeconnection = new JButton("Deconnection");
		btnDeconnection.setBounds(222, 12, 131, 25);
		panel.add(btnDeconnection);
		btnDeconnection.addActionListener(this);
		
        lblUsername=new JLabel(username);
		lblUsername.setBounds(35, 17, 150, 15);
		panel.add(lblUsername);
		
		//test Jlist apres on utilisera un get sur le modeleusers
		users = new Vector<String>();
		//users.addElement("alpha");
		//users.addElement("jojo");
				
		listUsers = new JList(users);
		listUsers.setBounds(12, 56, 340, 420);
		listUsers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listUsers.setLayoutOrientation(JList.VERTICAL);
		panel.add(listUsers);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 541, 793, 115);
		getContentPane().add(scrollPane);
	
		tAreaMessageText = new JTextArea();
		scrollPane.setViewportView(tAreaMessageText);
		
		
		btnSendMessage = new JButton("Send Message");
		btnSendMessage.setBounds(848, 554, 138, 25);
		getContentPane().add(btnSendMessage);
		btnSendMessage.addActionListener(this);
		
		btnSendFile = new JButton("Send File");
		btnSendFile.setBounds(848, 591, 138, 25);
		getContentPane().add(btnSendFile);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 50, 575, 472);
		getContentPane().add(scrollPane_1);
		
		tAreaHistoryCom = new JTextArea();
		tAreaHistoryCom.setEditable(false);
		scrollPane_1.setViewportView(tAreaHistoryCom);
		
		btnJoinFile = new JButton("Join File");
		btnJoinFile.setBounds(848, 634, 138, 25);
		getContentPane().add(btnJoinFile);
	}

	



	public void setUsers(Object[] objects) {
		for(int i=0;i<objects.length;i++){
			this.users.addElement((String) objects[i]);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		// TODO Auto-generated method stub
		//new Thread(new Runnable() {
		  //    public void run() {
		    	
				if(e.getSource()==btnDeconnection){
		  		controller.performDisconnect();
				}
				else if(e.getSource()==btnSendMessage){
					controller.performSendText(tAreaMessageText.getText());
					this.tAreaHistoryCom.setText(this.tAreaHistoryCom.getText()+"You"+" : "+this.tAreaMessageText.getText()+"\n");
					this.tAreaMessageText.setText("");;
				}
		  //    }
		  //}).start();
		
		
	}
}
