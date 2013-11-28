package org.insa.java.swing.view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.insa.java.view.JavaStandardMessageBar;

public class SwingStandardMessageBar extends JavaStandardMessageBar {
	protected JLabel textLabel = new JLabel();
	private JPanel container = new JPanel();

	public SwingStandardMessageBar() {
		container.setLayout(new BorderLayout());
		container.add(textLabel,BorderLayout.CENTER);
	}
	
	@Override
	public void setText(String s) {
		textLabel.setText(s);
	}

	@Override
	public Object getContainer() {
		return this.container;
	}

}
