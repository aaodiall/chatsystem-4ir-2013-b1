package org.insa.java.swing.view;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.insa.java.view.JavaStatusBar;

public class SwingStatusBar extends JavaStatusBar {

	private JPanel container = new JPanel();

	public SwingStatusBar() {
		this.emissionBar = new SwingFileTransferBar();
		this.receptionBar = new SwingFileTransferBar();
		this.messageBar = new SwingStandardMessageBar();

		container.setLayout(new GridLayout(3, 1));
		container.add((JPanel) messageBar.getContainer());
		container.add((JPanel) emissionBar.getContainer());
		container.add((JPanel) receptionBar.getContainer());
		
		this.messageBar.setText("No message");
		this.emissionBar.setText("No file transfer emmission processing");
		this.receptionBar.setText("No file transfer reception processing");
		
	}

	@Override
	public Object getContainer() {
		return this.container;
	}
}
