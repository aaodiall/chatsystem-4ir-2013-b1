/**
 * 
 */
package chatSystemIHMs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JButton;

/**represents the connected users window
 * @author Alpha DIALLO & Joanna VIGNE
 *Version 1.0
 */
public class InterfaceListUsers extends JFrame implements WindowListener,ActionListener, MouseListener{
	
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JList listUsers;
	private DefaultListModel users;
	private JLabel lblUsername;
	private JButton btnDeconnexion;
	private static ChatGUI staticChatgui;
	
	/**
	 * creates the application
	 * @param username
	 * @param chatgui
	 */
	public InterfaceListUsers(String username, ChatGUI chatgui) {
		setResizable(false);
		this.setTitle("Users Connected");		
		initWindows(username);
		staticChatgui=chatgui;
		this.setVisible(false);
	}
	
	/**
	 * Initialize the contents of the frame.
	 * @param username
	 * 			local user username
	 */			
	public void initWindows(String username){
		this.setBounds(100, 100, 331, 509);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		this.scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(12, 70, 300, 396);
		panel.add(scrollPane);
		
		users=new DefaultListModel();
		listUsers = new JList(users);
		this.listUsers.addMouseListener(this);
		listUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listUsers.setLayoutOrientation(JList.VERTICAL);
		scrollPane.setViewportView(listUsers);
		
		lblUsername = new JLabel(username);
		lblUsername.setBounds(12, 24, 162, 15);
		panel.add(lblUsername);
		
		btnDeconnexion = new JButton("Deconnexion");
		btnDeconnexion.addActionListener(this);
		btnDeconnexion.setBounds(195, 14, 125, 25);
		panel.add(btnDeconnexion);
	
		
	}
	/**
	 * sets the list of connected users
	 * @param objects
	 */
	public void setUsers(Object[] objects) {
		this.users.clear();
		for(int i=0;i<objects.length;i++){
			this.users.addElement(objects[i]);
		}
	}
	
	/* captures the event when the user pushes the disconnect button
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource()==btnDeconnexion){
			staticChatgui.disconnect();
		}
	}
/**
 * returns connected users list 
 * @return users
 */
	public DefaultListModel getUsers() {
		return users;
	}

	/**
	 * @return the lblUsername
	 */
	public String getLblUsername() {
		return lblUsername.getText();
	}


	/**
	 * @param username the lblUsername to set
	 */
	public void setLblUsername(String username) {
		this.lblUsername.setText(username); 
	}


	/* captures the event when the users clicks on a user of connected users list
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getClickCount()==2){//si double click sur un nom
			int index=this.listUsers.locationToIndex(arg0.getPoint());//trouve l'index du nom
			String user=(String) this.users.get(index);
			staticChatgui.openWindowCommunicate(user);
			this.listUsers.clearSelection();
		}		
	}


	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}