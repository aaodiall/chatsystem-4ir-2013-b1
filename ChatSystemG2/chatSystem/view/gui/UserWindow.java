package chatSystem.view.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

public class UserWindow extends JFrame{

	/**
	 * num�ro de version
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton deconnectButton;
	private JLabel username;
	private JList<String> contactsList;
	private DefaultListModel<String> contactsName;
	
	public UserWindow(String username, List<String> contactsName) {
		Collections.sort(contactsName);
		this.username = new JLabel(username);
		this.deconnectButton = new JButton("Deconnect");
		this.contactsName = new DefaultListModel<String>();
		for (String name: contactsName)
		{
			(this.contactsName).addElement(name);
		}
		this.contactsList = new JList<String>(this.contactsName);
		this.InitWindow();
	}
	
	private void InitWindow() {
		
		this.setTitle("Connect Window");
		this.setSize(300, 400);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new  GridBagConstraints();
		
		//contraintes pour le label username
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.ipady = 30;
		c.ipadx = 20;
		c.weightx = 0.0;
		panel.add(this.username, c);
		
		//contraintes pour le bouton d�connecter
		c.gridx = 3;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.ipady = 0;
		c.ipadx = 0;
		c.weightx = 0.0;
		panel.add(this.deconnectButton, c);
		
		//initialisation de la liste de contacts
		contactsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		contactsList.setLayoutOrientation(JList.VERTICAL);
		contactsList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(contactsList);
		listScroller.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
		listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//contraintes pour la liste de contacts
		c.gridx = 0;
		c.gridy = 5;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		c.gridheight = 5;
		c.ipady = 300;
		c.ipadx = 100;
		c.weightx = 0.0;
		panel.add(listScroller, c);
		
		this.add(panel, BorderLayout.PAGE_START);
		this.setVisible(true);
	}
	
	public void updateContacts(List<String> newContacts) {
		(this.contactsName).removeAllElements();
		Collections.sort(newContacts);
		for (String name: newContacts)
		{
			this.contactsName.addElement(name);
		}
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("anthony");
		list.add("anthony");
		list.add("alpha");
		list.add("jules");
		list.add("yann");
		list.add("joanna");
		list.add("guillaume");
		list.add("pierre");
		list.add("joffrey");
		list.add("aymeric");
		list.add("thomas");
		UserWindow f = new UserWindow("marjorie", list);
		list.add("marie");
		f.updateContacts(list);
		list.remove("guillaume");
		f.updateContacts(list);
	}
}
