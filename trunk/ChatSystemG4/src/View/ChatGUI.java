package ChatSystem;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class ChatGUI extends JFrame implements ActionListener, MouseListener{
	private static final long serialVersionUID = 1L;
	ChatController chatController;
	JTextArea taChat;
	JTextArea taSendText;
	JSplitPane spHorizontal;
	JSplitPane spVertical;
	JButton bSend;
	JButton bSendFile;
	JList<String> lUsers;
	JPanel pChat;
	JPanel pUsers;
	JPanel pSend;
	String lastSelectedItem;
	String s;
	JFileChooser fc;
	JLabel lChattingWith;

	String[] data = {"one", "two", "three", "four"}; //Change in favor of the User type list

	public ChatGUI (ChatController controller){
		chatController = controller;
		//Future: personalize buttons and change text
		do{
			s = (String)JOptionPane.showInputDialog(
					this,
					"Type your username and press accept to connect.\nUsername: ",
					"Chat System Connection",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					"");
			if(s != null && ("".equals(s)))
				System.out.println("No username entered, enter a username please.");
		}while(s != null && ("".equals(s)));
		//If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			System.out.println("Username: "+s);
			chatController.connect(s);
			setTitle(s+" - Chat System");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			initComponents();
		}else{
			System.out.println("Cancel is pressed");
			System.exit(-1);
		}
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		pUsers = createUsersPanel();
		pChat = createChatPanel();
		this.add(pUsers, BorderLayout.LINE_START);
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(pChat);
		this.pack();
		this.setVisible(true);
	}


	private JPanel createUsersPanel() {
		JPanel pane = new JPanel();
		lUsers = new JList<String>(data);
		lUsers.addMouseListener(this);
		pane.add(lUsers);
		return pane;
	}

	private JPanel createSendPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout());
		bSend = new JButton("Send");
		bSend.addActionListener(this);
		bSendFile = new JButton("+");
		bSendFile.addActionListener(this);
		taSendText = new JTextArea(); //Add properties to textArea
		pane.add(taSendText);
		pane.add(bSend);
		pane.add(bSendFile);
		return pane;
	}

	private JPanel createChatPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		//lChattingWith = new JLabel("");
		taChat = new JTextArea(10, 50);
		//taChat.setDropMode(DropMode.ON); //For future improvements, maybe
		pSend = createSendPanel();		
		spHorizontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				taChat, pSend);
		//pane.add(lChattingWith);
		pane.add(spHorizontal);
		return pane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Send")){
			//Change: Send text to the user and append to chat
			taChat.append(s + "[" + getCurrentTime() + "]: "+taSendText.getText()+"\n");
			//Change
		}else if(e.getActionCommand().equals("+")){
			//Change: Open file chooser and get the file's path to send it
			fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            //This is where a real application would open the file.
	            taChat.append("Selected file: " + file.getName() + ".\n");
	            taChat.append("File's path: " + file.getPath() + "\n");
	        } else {
	            System.out.println("Open command cancelled by user.");
	        }
			taChat.append("Sending File...\n");
			//Change
		}
	}

	private String getCurrentTime() {
		Date now = new Date();
		String currentTime = new SimpleDateFormat("HH:mm:ss").format(now);
		return currentTime;
	}

	@Override
	public void mouseClicked(MouseEvent m) {
		if (m.getClickCount() == 2) {
			String selectedItem = (String) lUsers.getSelectedValue();
			if(!selectedItem.equals(lastSelectedItem)){
				//lChattingWith.setText(selectedItem);
				//Change: Charge the talk with the selected user
				taChat.append("Chatting with: "+selectedItem);
				//Change
				lastSelectedItem = selectedItem;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent m) {}

	@Override
	public void mouseExited(MouseEvent m) {}

	@Override
	public void mousePressed(MouseEvent m) {}

	@Override
	public void mouseReleased(MouseEvent m) {}
	
}