package org.insa.general.view;

public abstract class GeneralStatusBar {
	
	public abstract void setProgressBarValue(int i);
	
	public abstract void setProgressBarVisible(boolean b);
	
	public abstract void setProgressBarMax(int max);
	
	public abstract void resetProgressBar();
	
	public abstract void setTextLabel(String s);
	
	public abstract Object getStatusBar();
}
