package org.insa.java.swing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.insa.java.view.JavaFileTransferBar;

public class SwingFileTransferBar extends JavaFileTransferBar{
	protected JLabel textLabel;
	protected JProgressBar progressBar;
	protected JButton cancelButton;
	protected JPanel container = new JPanel();
	protected JPanel buttonContainer = new JPanel();
	
	public SwingFileTransferBar() {
		this.textLabel = new JLabel();
		this.progressBar = new JProgressBar();
		this.cancelButton = new JButton("x");
		cancelButton.setPreferredSize(new Dimension(15,15));
		cancelButton.setVisible(false);
		
		buttonContainer.setLayout(new BorderLayout());
		buttonContainer.add(progressBar,BorderLayout.CENTER);
		buttonContainer.add(cancelButton,BorderLayout.EAST);

		container.setLayout(new GridLayout(1,2));
		container.add(textLabel);
		container.add(buttonContainer);
		progressBar.setVisible(false);
	}

	@Override
	public void setText(String s) {
		textLabel.setText(s);
	}

	@Override
	public void setValue(int i) {
		progressBar.setValue(i);
	}

	@Override
	public void reset() {
		progressBar.setValue(0);
	}

	@Override
	public void setMax(int i) {
		progressBar.setMaximum(i);
	}

	@Override
	public Object getContainer() {
		return this.container;
	}

	@Override
	public void setVisible(boolean b) {
		buttonContainer.setVisible(b);
	}

	@Override
	public int getValue() {
		return progressBar.getValue();
	}

	@Override
	public Object getCancelbutton() {
		return cancelButton;
	}
}
