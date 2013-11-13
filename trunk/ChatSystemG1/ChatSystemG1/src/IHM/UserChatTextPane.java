package IHM;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UserChatTextPane extends JScrollPane {
	
	
	private JTextArea textArea;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7124400056327705310L;

	public UserChatTextPane() {
		// TODO Auto-generated constructor stub
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.setViewportView(textArea);
		this.setVisible(true);

	}

	/**
	 * @return the textArea
	 */
	public JTextArea getTextArea() {
		return textArea;
	}

}
