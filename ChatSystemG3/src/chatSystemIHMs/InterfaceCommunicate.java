/**
 * 
 */
package chatSystemIHMs;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JProgressBar;
import java.awt.Color;

/**
 * represents the conversation window
 *@author Alpha DIALLO & Joanna VIGNE
 *@version 1.0
 */
public class InterfaceCommunicate extends JFrame implements ActionListener{
	
	
	private static final long serialVersionUID = 1L;
	private JTextArea tAreaHistoryCom;
	private JButton btnSendFile;
	private JButton btnSendMessage;
	private JTextArea tAreaMessageText;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JProgressBar progressBarFile;
	private JButton btnFileReceived;
	private ChatGUI chatGUI;
	
	
	/**
	 * Create the application.
	 * @param usernameRemote
	 * @param chatGUI
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
		
		progressBarFile = new JProgressBar();
		progressBarFile.setEnabled(true);
		progressBarFile.setVisible(false);
		progressBarFile.setMaximum(100);
		progressBarFile.setMinimum(0);
		progressBarFile.setForeground(Color.GREEN);
		progressBarFile.setIndeterminate(false);
		progressBarFile.setBounds(449, 12, 148, 14);
		getContentPane().add(progressBarFile);
		
		btnFileReceived = new JButton(new ImageIcon("image/download_opt.png"));
		btnFileReceived.addActionListener(this);
		btnFileReceived.setBackground(Color.WHITE);
		btnFileReceived.setBounds(617, 12, 29, 18);
		this.btnFileReceived.setVisible(false);
		getContentPane().add(btnFileReceived);
		this.setResizable(false);
	
	}

/**
 * returns the FileReceived button
 * @return btnFileReceived
 */
	
	public JButton getBtnFileReceived() {
		return btnFileReceived;
	}

/**
 * returns the progressBarFile 
 * @return progressBarFile
 */
	public JProgressBar getProgressBarFile() {
		return progressBarFile;
	}

/**
 * sets the progressBarFile
 * @param progressBarFile
 */
	public void setProgressBarFile(JProgressBar progressBarFile) {
		this.progressBarFile = progressBarFile;
	}


	/* captures the events from the user in a conversation window
	 * (non-Javadoc)
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
		}else if (e.getSource()==this.btnFileReceived){
			this.chatGUI.notifyFileReceived(this.getTitle());
		}
		
	}

	
/**
 * returns JTextArea which is the field where messages is displayed
 * @return tAreaHistoryCom
 */
	public JTextArea gettAreaHistoryCom() {
		return tAreaHistoryCom;
	}

/**
 * sets the text of JTextArea which is the field where messages is displayed
 * @param text
 * 			the message write by the user
 */
	public void settAreaHistoryCom(String text) {
		this.tAreaHistoryCom.append(text);;
	}

/**
 * returns the text of JTextArea which is the field where the user write his messages
 * @return tAreaMessageText
 * 
 */
	public String gettAreaMessageText() {
		return tAreaMessageText.getText();
	}
/**
 * sets the text of JTextArea which is the field where the user write his messages
 * @param tAreaMessageText
 */
	public void settAreaMessageText(String tAreaMessageText) {
		this.tAreaMessageText.setText(tAreaMessageText);
	}
}
