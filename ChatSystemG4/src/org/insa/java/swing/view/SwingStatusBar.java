package org.insa.java.swing.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import org.insa.java.controller.ChatController;
import org.insa.java.view.JavaStatusBar;

public class SwingStatusBar extends JavaStatusBar implements ActionListener{

	private JPanel container = new JPanel();

	public SwingStatusBar(ChatController chatController) {
		this.chatController = chatController;
		this.emissionBar = new SwingFileTransferBar();
		this.receptionBar = new SwingFileTransferBar();
		this.messageBar = new SwingStandardMessageBar();

		container.setLayout(new GridLayout(3, 1));
		container.add((JPanel) messageBar.getContainer());
		container.add((JPanel) emissionBar.getContainer());
		container.add((JPanel) receptionBar.getContainer());
		
		((AbstractButton)emissionBar.getCancelbutton()).addActionListener(this);
		((AbstractButton)receptionBar.getCancelbutton()).addActionListener(this);
		
		this.messageBar.setText("No message");
		this.emissionBar.setText("No file transfer emmission processing");
		this.receptionBar.setText("No file transfer reception processing");
	}

	@Override
	public Object getContainer() {
		return this.container;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == emissionBar.getCancelbutton()) {
			chatController.fileEmissionCanceled();
		}
		else if(e.getSource() == receptionBar.getCancelbutton()) {
			chatController.fileReceptionCanceled();
		}
	}
}
