package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Controller.ChatController;
import Interface.ControllerToGui;
import Model.ChatModel;
import Model.User;

public class ChatGUI extends JFrame implements ControllerToGui{
	private static final long serialVersionUID = -7473290932142595058L;
	
	private final int width = 600;
	private final int height = 800;
	
	private ChatController chatController;
	private String localUsername;
	
	private JTextPane chatTextPane = new JTextPane();
	private StyledDocument chatStyledDocument;
	private Style localUserStyle;
	private Style remoteUserStyle;
	private Style normalTextStyle;
	
	private JTextArea sendTextArea = new JTextArea();
	private JButton sendButton = new JButton("Send");
	private JButton sendFileButton = new JButton("+");
	private JList<User> userList = new JList<User>();
	private JPanel chatPanel = new JPanel();
	private JPanel sendPanel = new JPanel();
	private JPanel formPanel = new JPanel();
	private JFileChooser fileChooser;
	
	public ChatGUI (){
		
		localUsername = JOptionPane.showInputDialog(this,"Type your username and press accept to connect.\nUsername: ","Chat System Connection",JOptionPane.PLAIN_MESSAGE);
		
		if ((localUsername == null) || (localUsername.length() <= 0)) {
			System.out.println("Cancel is pressed");
			System.exit(-1);
		}
		
		//Rend le username unique en lui accolant le nom de la machine
		try {
			localUsername += "@"+InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		System.out.println("Username : "+ localUsername);		
		initComponents();
		initActions();
		ChatModel model = new ChatModel(new User(null,localUsername));
		userList.setModel(model);
		chatController = new ChatController(model,this);
		chatController.sendHelloMessage(model.getLocalUser());	
	}

	private void initComponents() {		
		chatStyledDocument = chatTextPane.getStyledDocument();
		localUserStyle = chatStyledDocument.addStyle("LocalUserStyle", null);
		remoteUserStyle = chatStyledDocument.addStyle("remoteUserStyle", null);
		normalTextStyle = chatStyledDocument.addStyle("normalUserStyle",null);
		StyleConstants.setForeground(localUserStyle, Color.GREEN);
		StyleConstants.setForeground(remoteUserStyle, Color.ORANGE);
		StyleConstants.setForeground(normalTextStyle, Color.BLACK);
		
		sendPanel.setLayout(new GridLayout(2,1));
		sendPanel.add(sendButton);
		sendPanel.add(sendFileButton);
		
		formPanel.setLayout(new BorderLayout());
		formPanel.add(new JScrollPane(sendTextArea),BorderLayout.CENTER);
		formPanel.add(sendPanel,BorderLayout.EAST);
		
		chatPanel.setLayout(new BorderLayout());
		chatPanel.add(chatTextPane,BorderLayout.CENTER);
		chatPanel.add(formPanel,BorderLayout.SOUTH);
		chatPanel.setMinimumSize(new Dimension((int) (0.7*width), chatPanel.getHeight()));
		
		this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatPanel, userList));
		
		this.setTitle(localUsername + " - Chat System");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void initActions() {
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				User selectedUser = userList.getSelectedValue();
	        	if(selectedUser != null)
	        		chatController.sendTextMessage(selectedUser, sendTextArea.getText());
			}
		});
		
		sendFileButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(getParent());

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = fileChooser.getSelectedFile();
		            chatController.sendFile(userList.getSelectedValue(), selectedFile);
		        } else {
		            System.out.println("Open command cancelled by user.");
		        }
			}
		});
		
		userList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				chatTextPane.setText(chatController.getTalk(userList.locationToIndex(evt.getPoint())));
			}
		});
		
		sendTextArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		        	User selectedUser = userList.getSelectedValue();
		        	if(selectedUser != null)
		        		chatController.sendTextMessage(selectedUser, sendTextArea.getText());
		        }
		    }
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				chatController.sendGoodbyeMessage(chatController.getLocalUser());
			}
		});
	}

	public JList<User> getUserList() {
		return userList;
	}
	
	public void setChatText(String newTalk) {
		chatTextPane.setText(newTalk);
	}

	public int showConfirmationPane(String username) {
		return JOptionPane.showConfirmDialog(null,
				"The user "+ username +" wants to send you a file. \nDo you want to accept the file transfer?",
				"File transfer demand", JOptionPane.YES_NO_OPTION);
	}

	public String getSavingPath() {
		fileChooser = new JFileChooser(); 
		fileChooser.setDialogTitle("Select directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int value = fileChooser.showOpenDialog(getParent());
		while(value != JFileChooser.APPROVE_OPTION){
			JOptionPane.showMessageDialog(getParent(), "You must select a directory.");
			value = fileChooser.showOpenDialog(getParent());
		}
		return fileChooser.getSelectedFile().toString();
	}
}