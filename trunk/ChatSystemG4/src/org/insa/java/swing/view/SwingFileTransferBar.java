package org.insa.java.swing.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.insa.java.view.JavaFileTransferBar;

public class SwingFileTransferBar extends JavaFileTransferBar {
	protected JLabel textLabel;
	protected JProgressBar progressBar;
	private JPanel container = new JPanel();
	
	public SwingFileTransferBar() {
		this.textLabel = new JLabel();
		this.progressBar = new JProgressBar();

		container.setLayout(new GridLayout(1,2));
		container.add(textLabel);
		container.add(progressBar);
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
		progressBar.setVisible(b);
	}

	@Override
	public int getValue() {
		return progressBar.getValue();
	}

}
