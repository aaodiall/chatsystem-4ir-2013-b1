package IHM;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import Controller.ChatController;
import Controller.Observer;

public class FrontController extends Observer implements Runnable,ActionListener {
	private MainFrame mFrame;
	private static int fontSize;
	private static Font selectedFont;
	
	public FrontController() {
		// TODO Auto-generated constructor stub
		this.mFrame = new MainFrame();
		mFrame.getMenu().getMOO().getMenuItemAbout().addActionListener(this);
		mFrame.getMenu().getMOO().getMenuItemSelectFont().addActionListener(this);
		mFrame.getMenu().getMOO().getMenuItemSendFile().addActionListener(this);
		mFrame.getMenu().getMGO().getMenuItemClose().addActionListener(this);
		mFrame.getMenu().getMGO().getMenuItemConnect().addActionListener(this);
		mFrame.getMenu().getMGO().getMenuItemDisconnect().addActionListener(this);
		this.fontSize = 12;
		this.selectedFont = new Font("Times New Roman", Font.PLAIN, fontSize);
		Thread t = new Thread(this, "Receiver Thread");
	     t.start(); // Start the thread
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == mFrame.getMenu().getMOO().getMenuItemSelectFont()){
			JOptionPane.showMessageDialog(null,
				    "Bientot ici choix de la Font des text Area","selectfont",JOptionPane.INFORMATION_MESSAGE);
		}

		if(e.getSource() == mFrame.getMenu().getMOO().getMenuItemAbout()){
			JOptionPane.showMessageDialog(null,
				    "ChatSystem V0.0.1\nYann Label\nAymeric Duchein","About",JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(e.getSource() == mFrame.getMenu().getMOO().getMenuItemSendFile()){
			JOptionPane.showMessageDialog(null,
				    "Bientot ici choix pour envoyer des Fichiers","File",JOptionPane.INFORMATION_MESSAGE);
		}
		if(e.getSource() == mFrame.getMenu().getMGO().getMenuItemClose()){
			this.mFrame.setVisible(false);
		}
		if(e.getSource() == mFrame.getMenu().getMGO().getMenuItemConnect()){
			if(!ChatController.isConnected()){	
				String s = (String)JOptionPane.showInputDialog(
						   null,
						   "Veuillez indiquez votre pseudo",
						   "Connexion",
						   JOptionPane.QUESTION_MESSAGE,
						   null, 
						   null, "Pseudonyme"); 
						if ((s != null) && (s.length() > 0)){
						   try {
							ChatController.setLocalUsername(s);
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						ChatController.PerformConnect();
						mFrame.getMenu().getMGO().getMenuItemConnect().setEnabled(false);
						mFrame.getMenu().getMGO().getMenuItemDisconnect().setEnabled(true);
						}else{
							JOptionPane.showMessageDialog(null,
								    "Vous n'avez pas entré de pseudo","Attention!",JOptionPane.WARNING_MESSAGE);
						}
							

			}
		}
		if(e.getSource() == mFrame.getMenu().getMGO().getMenuItemDisconnect()){
			if(ChatController.isConnected()){
				
							ChatController.PerformDisconnect();
							mFrame.getMenu().getMGO().getMenuItemConnect().setEnabled(true);
							mFrame.getMenu().getMGO().getMenuItemDisconnect().setEnabled(false);

			}
			
		}
	}

	/**
	 * @return the mFrame
	 */
	public MainFrame getmFrame() {
		return mFrame;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(mFrame.isVisible()){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
}
