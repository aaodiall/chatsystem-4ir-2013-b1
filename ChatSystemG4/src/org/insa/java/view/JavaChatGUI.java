package org.insa.java.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.insa.controller.ChatController;
import org.insa.general.view.GeneralChatGUI;
import org.insa.model.User;

import Interface.ControllerToGui;

public class JavaChatGUI extends GeneralChatGUI implements ControllerToGui, ActionListener, MouseListener, KeyListener, WindowListener{
	private final int WIDTH = 600;
	private final int HEIGHT = 800;

	private JTextPane chatTextPane = new JTextPane();
	private StyledDocument chatStyledDocument;
	private Style localUserStyle;
	private Style remoteUserStyle;
	private Style normalTextStyle;

	private JFrame mainWindow = new JFrame();
	private JTextArea sendTextArea = new JTextArea();
	private JButton sendButton = new JButton("Send");
	private JButton sendFileButton = new JButton("+");
	private JList<User> userList = new JList<User>();
	private JLabel statusLabel = new JLabel("Aucune notification");
	private JProgressBar progressBar = new JProgressBar(0, 100);
	private JPanel chatPanel = new JPanel();
	private JPanel sendPanel = new JPanel();
	private JPanel formPanel = new JPanel();
	private JPanel statusPanel = new JPanel();

	public JavaChatGUI() {
		chatController = new ChatController(this);		
		this.connect();
		this.initComponents();
	}
	
	private void initComponents() {		
		userList.setModel(chatController.getModel());
		chatStyledDocument = chatTextPane.getStyledDocument();
		localUserStyle = chatStyledDocument.addStyle("LocalUserStyle", null);
		remoteUserStyle = chatStyledDocument.addStyle("remoteUserStyle", null);
		normalTextStyle = chatStyledDocument.addStyle("normalUserStyle",null);
		StyleConstants.setForeground(localUserStyle, Color.GREEN);
		StyleConstants.setForeground(remoteUserStyle, Color.ORANGE);
		StyleConstants.setForeground(normalTextStyle, Color.BLACK);

		statusPanel.setLayout(new BorderLayout());
		statusPanel.add(statusLabel,BorderLayout.CENTER);
		statusPanel.add(progressBar,BorderLayout.EAST);
		//progressBar.setVisible(false);
		
		sendPanel.setLayout(new GridLayout(2,1));
		sendPanel.add(sendButton);
		sendPanel.add(sendFileButton);

		formPanel.setLayout(new BorderLayout());
		formPanel.add(new JScrollPane(sendTextArea),BorderLayout.CENTER);
		formPanel.add(sendPanel,BorderLayout.EAST);

		chatPanel.setLayout(new BorderLayout());
		chatPanel.add(chatTextPane,BorderLayout.CENTER);
		chatPanel.add(formPanel,BorderLayout.SOUTH);
		chatPanel.setMinimumSize(new Dimension((int) (0.7*WIDTH), chatPanel.getHeight()));

		sendButton.addActionListener(this);
		sendFileButton.addActionListener(this);
		userList.addMouseListener(this);
		sendTextArea.addKeyListener(this);
		mainWindow.addWindowListener(this);
		
		mainWindow.setLayout(new BorderLayout());
		mainWindow.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatPanel, userList),BorderLayout.CENTER);
		mainWindow.add(statusPanel,BorderLayout.SOUTH);
		mainWindow.setTitle("Chat System");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(WIDTH, HEIGHT);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setVisible(true);
		
		statusPanel.setPreferredSize(new Dimension(mainWindow.getWidth(),15));	
	}

	@Override
	public String usernameInput() {
		return JOptionPane.showInputDialog(mainWindow,"Type your username and press accept to connect.\nUsername: ","Chat System Connection",JOptionPane.PLAIN_MESSAGE);
	}
	
	@Override
	public void setChatText(String newTalk) {
		chatTextPane.setText(newTalk);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sendButton)
			this.sendTextMessage(userList.getSelectedValue(), sendTextArea.getText());
		else if(e.getSource() == sendFileButton) {
			JFileChooser fileChooser = new JFileChooser();
			int returnVal = fileChooser.showOpenDialog(mainWindow.getParent());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.sendFile(userList.getSelectedValue(), fileChooser.getSelectedFile());
			}
		}
			
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == userList)
			chatController.getTalk(userList.locationToIndex(e.getPoint()));
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getSource() == sendTextArea && e.getKeyCode() == KeyEvent.VK_ENTER)
			this.sendTextMessage(userList.getSelectedValue(), sendTextArea.getText());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.disconnect();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	public JFrame getMainWindow() {
		return mainWindow;
	}

	@Override
	public int getSelectedIndex() {
		return userList.getSelectedIndex();
	}

	@Override
	public String getFilePath() {
		JFileChooser fileChooser = new JFileChooser(); 
		fileChooser.setDialogTitle("Where do you want to save the file ?");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int value = fileChooser.showOpenDialog(mainWindow);
		while(value != JFileChooser.APPROVE_OPTION){
			JOptionPane.showMessageDialog(mainWindow, "You must select a directory.");
			value = fileChooser.showOpenDialog(mainWindow);
		}
		return fileChooser.getSelectedFile().toString();
	}

	@Override
	public void showFileTransferProgress(String text, int progress) {
		statusLabel.setText(text);
		progressBar.setValue(progress);
	}
}
