package org.insa.java.swing.view;

import java.awt.BorderLayout;
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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.insa.java.controller.ChatController;
import org.insa.java.model.User;
import org.insa.java.view.JavaChatGUI;

import Interface.ControllerToGui;

public class SwingChatGUI extends JavaChatGUI implements ControllerToGui, ActionListener, MouseListener, KeyListener, WindowListener{
	private final int WIDTH = 800;
	private final int HEIGHT = 800;

	private JTextPane chatTextPane = new JTextPane();

	private JFrame mainWindow = new JFrame();
	private JTextArea sendTextArea = new JTextArea();
	private JButton sendButton = new JButton("Send");
	private JButton sendFileButton = new JButton("+");
	private JList<User> userList = new JList<User>();
	private JPanel chatPanel = new JPanel();
	private JPanel sendPanel = new JPanel();
	private JPanel formPanel = new JPanel();

	public SwingChatGUI() {
		chatController = new ChatController(this);	
		statusBar = new SwingStatusBar(chatController);
		this.connect();
		this.initComponents();
	}
	
	private void initComponents() {		
		userList.setModel(chatController.getModel());
	
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
		mainWindow.add((JPanel)statusBar.getContainer(),BorderLayout.SOUTH);
		mainWindow.setTitle("Chat System");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(WIDTH, HEIGHT);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setVisible(true);
		
		//((JPanel)statusBar.getContainer()).setPreferredSize(new Dimension(mainWindow.getWidth(),40));	
		
		//Equivalent to disconnect method
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				chatController.sendGoodbyeMessage(chatController.getModel().getLocalUser());
			}
		});
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
	public void keyPressed(KeyEvent e) {
		if(e.getSource() == sendTextArea && e.getKeyCode() == KeyEvent.VK_ENTER)
			this.sendTextMessage(userList.getSelectedValue(), sendTextArea.getText());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == userList)
			chatController.updateTalk(userList.locationToIndex(e.getPoint()));
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		this.disconnect();
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
		fileChooser.showOpenDialog(mainWindow);
		return fileChooser.getSelectedFile().toString();
	}

	@Override
	public Object getFrame() {
		return mainWindow;
	}
	
	
	//Unused actions
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
}
