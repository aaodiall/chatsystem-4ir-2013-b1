package IHM;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class MessageTextPane extends JScrollPane {
	private JTextArea textfield;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3351616799647526645L;

	/**
	 * @return the textfield
	 */
	public JTextArea getTextfield() {
		return textfield;
	}

	public MessageTextPane() {
		//this.setMinimumSize(new Dimension(500,200));
		this.setPreferredSize(new Dimension(100,120));
		// TODO Auto-generated constructor stub
		this.textfield = new JTextArea();
		this.setViewportView(textfield);
		this.setVisible(true);

	}

}
