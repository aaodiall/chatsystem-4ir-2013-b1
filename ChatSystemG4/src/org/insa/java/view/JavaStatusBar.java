package org.insa.java.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.insa.general.view.GeneralStatusBar;

public class JavaStatusBar extends GeneralStatusBar{
	private JPanel statusPanel = new JPanel();
	private JLabel statusLabel = new JLabel();
	private JProgressBar statusProgress = new JProgressBar();
	
	public JavaStatusBar() {
		super();
		statusPanel.setLayout(new GridLayout(1, 2));
		statusPanel.add(statusLabel);
		statusPanel.add(statusProgress);
	}
	
	public void setProgressBarVisible(boolean b) {
		this.statusProgress.setVisible(b);
	}
	
	public void setProgressBarMax(int max) {
		this.statusProgress.setMaximum(max);
	}
	
	public void resetProgressBar() {
		this.statusProgress.setValue(0);
	}
	
	public void setTextLabel(String s) {
		this.statusLabel.setText(s);
	}

	public JPanel getStatusPanel() {
		return statusPanel;
	}
	
	public void setProgressBarValue(int i) {
		this.statusProgress.setValue(i);
	}

	@Override
	public Object getStatusBar() {
		return statusPanel;
	}
}
