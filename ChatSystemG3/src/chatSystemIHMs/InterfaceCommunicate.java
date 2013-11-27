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
import javax.swing.UIManager;
import java.awt.Font;

/**
 * @author alpha
 *
 */
public class InterfaceCommunicate extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private Controller controller;
	//private Vector<String> users;
	//private JButton btnJoinFile;
	private JTextArea tAreaHistoryCom;
	private JButton btnSendFile;
	private JButton btnSendMessage;
	private JTextArea tAreaMessageText;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private ChatGUI chatGUI;
	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the application.
	 */
	public InterfaceCommunicate(String usernameRemote,ChatGUI chatGUI){
		this.chatGUI=chatGUI;		
		this.setTitle(usernameRemote);		
		initialize();		
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		/*tab.add("tab",new JPanel() );*/
		
		this.setBounds(100, 5, 663, 702);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(UIManager.getColor("Button.select"));
		getContentPane().setLayout(null);
		
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 541, 499, 115);
		getContentPane().add(scrollPane);
	
		tAreaMessageText = new JTextArea();
		this.tAreaMessageText.setLineWrap(true);
		scrollPane.setViewportView(tAreaMessageText);
		
		
		btnSendMessage = new JButton("Send Message");
 		btnSendMessage.setBounds(512, 554, 138, 25);
 		
		getContentPane().add(btnSendMessage);
		btnSendMessage.addActionListener(this);
		
		btnSendFile = new JButton("Send File");
		btnSendFile.setBounds(512, 591, 138, 25);
		getContentPane().add(btnSendFile);
		this.btnSendFile.addActionListener(this);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 50, 585, 472);
		getContentPane().add(scrollPane_1);
		
		tAreaHistoryCom = new JTextArea();
		tAreaHistoryCom.setFont(new Font("Dialog", Font.BOLD, 14));
		tAreaHistoryCom.setEditable(false);
		this.tAreaHistoryCom.setLineWrap(true);
		scrollPane_1.setViewportView(tAreaHistoryCom);
		this.getRootPane().setDefaultButton(btnSendMessage);
		this.setResizable(false);
	
	}

	



	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource()==btnSendMessage){
			if(!tAreaMessageText.getText().equals("")){
				this.chatGUI.sendMessage(this.getTitle());
			}
		}
		else if (e.getSource()==this.btnSendFile){
			this.chatGUI.sendFile(this.getTitle());
		}
		
	}

	

	public JTextArea gettAreaHistoryCom() {
		return tAreaHistoryCom;
	}


	public void settAreaHistoryCom(String text) {
		this.tAreaHistoryCom.append(text);;
	}


	public String gettAreaMessageText() {
		return tAreaMessageText.getText();
	}

	public void settAreaMessageText(String tAreaMessageText) {
		this.tAreaMessageText.setText(tAreaMessageText);
	}
}
