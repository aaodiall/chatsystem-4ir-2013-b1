package IHM;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;

import Controller.ChatController;
import Controller.Conversation;
import Controller.Observer;

public class FrontController extends Observer implements Runnable,ActionListener {
	private MainFrame mFrame;
	
	private Font selectedFont;
	
	
	public FrontController() {
		// TODO Auto-generated constructor stub
		
		this.mFrame = new MainFrame();
		mFrame.getMenu().getMOO().getMenuItemAbout().addActionListener(this);
		mFrame.getMenu().getMOO().getMenuItemSelectFont().addActionListener(this);
		mFrame.getMenu().getMOO().getMenuItemSendFile().addActionListener(this);
		mFrame.getMenu().getMGO().getMenuItemClose().addActionListener(this);
		mFrame.getMenu().getMGO().getMenuItemConnect().addActionListener(this);
		mFrame.getMenu().getMGO().getMenuItemDisconnect().addActionListener(this);
		mFrame.getULpane().getULChooseUser().addActionListener(this);
		mFrame.getULpane().getMSSend().addActionListener(this);
		
		this.selectedFont = new Font("Times New Roman", Font.PLAIN, 12);
		Thread t = new Thread(this, "Receiver Thread");
	     t.start(); // Start the thread
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == mFrame.getULpane().getMSSend()){
			if(mFrame.getUCpane().getSelectedTab() == -1){
				JOptionPane.showMessageDialog(null,
					    "Vous n'avez pas choisi de destinataire","Attention!",JOptionPane.WARNING_MESSAGE);
			}else{
				String[] s = new String[1];
				s[0]=   mFrame.getUCpane().getTitleAt(mFrame.getUCpane().getSelectedTab());
				ChatController.PerformSendMessage(mFrame.getMSpane().getMTpane().getTextfield().getText(),s);
				mFrame.getMSpane().getMTpane().getTextfield().setText("");
			}
		}
		if(e.getSource() == mFrame.getULpane().getULChooseUser()){
			boolean exist = false;
			int tabnum = 0;
			if(mFrame.getULpane().getULLpane().getUserList().getSelectedValue() != null){
			for(int i = 0;i<mFrame.getUCpane().getTabCount();i++){
				if(mFrame.getULpane().getULLpane().getUserList().getSelectedValue() == mFrame.getUCpane().getTitleAt(i)){
					exist = true;
					tabnum = i;
				}
			}
			
			if(!exist){
				 mFrame.getUCpane().AddTab((String) mFrame.getULpane().getULLpane().getUserList().getSelectedValue());
				 mFrame.getUCpane().setSelectedIndex(mFrame.getUCpane().getTabCount()-1);
			}else{
				mFrame.getUCpane().setSelectedIndex(tabnum);
			}
			}else{
				JOptionPane.showMessageDialog(null,
					    "Vous n'avez pas selection d'User","Attention!",JOptionPane.WARNING_MESSAGE);
			}
			
		}
		
		if(e.getSource() == mFrame.getMenu().getMOO().getMenuItemSelectFont()){
			FontChooser jf = new FontChooser();
			selectedFont = jf.showDialog(null, "Choose a font");
            JOptionPane.showMessageDialog(null, selectedFont == null ? "You canceled the dialog."
                    : "You have selected " + selectedFont.getName() + ", " + selectedFont.getSize()
                    + (selectedFont.isBold() ? ", Bold" : "") + (selectedFont.isItalic() ? ", Italic" : ""));
            mFrame.getMSpane().getMTpane().getTextfield().setFont(selectedFont);
           
            for(int i = 0;i<mFrame.getUCpane().getTabCount();i++){
            	mFrame.getUCpane().getTextAreas().get(i).getTextArea().setFont(selectedFont);
            }
            
		}

		if(e.getSource() == mFrame.getMenu().getMOO().getMenuItemAbout()){
			JOptionPane.showMessageDialog(null,
				    "ChatSystem V0.0.2\nYann Label\nAymeric Duchein","About",JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(e.getSource() == mFrame.getMenu().getMOO().getMenuItemSendFile()){
			
			
			  FileChooser fc = new FileChooser();
			  fc.showDialog(null, "Envoyer");
			 java.io.File f = fc.getSelectedFile();
			 if (mFrame.getUCpane().getSelectedTab() == -1){
				 JOptionPane.showMessageDialog(null,
						    "Vous n'avez pas selection d'User","Attention!",JOptionPane.WARNING_MESSAGE);
			 }else{
				 ChatController.PerformFileAcceptance(f, mFrame.getUCpane().getTitleAt(mFrame.getUCpane().getSelectedTab()));
			 }
			
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
						if(mFrame.getUCpane().getTabCount() != 0){
							if(mFrame.getUCpane().getTitleAt(0) == "bonjour"){
								mFrame.getUCpane().closeTab(0);
							}
						}
						mFrame.getMenu().getMGO().getMenuItemConnect().setEnabled(false);
						mFrame.getMenu().getMGO().getMenuItemDisconnect().setEnabled(true);
						mFrame.getULpane().getULChooseUser().setEnabled(true);
						mFrame.getULpane().getMSSend().setEnabled(true);
						}else{
							JOptionPane.showMessageDialog(null,
								    "Vous n'avez pas entr� de pseudo","Attention!",JOptionPane.WARNING_MESSAGE);
						}
							

			}
		}
		if(e.getSource() == mFrame.getMenu().getMGO().getMenuItemDisconnect()){
			if(ChatController.isConnected()){
				
							ChatController.PerformDisconnect();
							mFrame.getMenu().getMGO().getMenuItemConnect().setEnabled(true);
							mFrame.getMenu().getMGO().getMenuItemDisconnect().setEnabled(false);
							mFrame.getULpane().getULChooseUser().setEnabled(false);
							mFrame.getULpane().getMSSend().setEnabled(false);

			}
			
		}
	}

	/**
	 * @return the mFrame
	 */
	public MainFrame getmFrame() {
		return mFrame;
	}
	
	public void Notify(int notif){
		if(notif == 0){
			
			this.mFrame.getULpane().getULLpane().getUserList().setListData(ChatController.getUserList().toArray());
		}else {
			//conversation ici
			Conversation c = ChatController.getConv(notif-1);
			String s = c.getUserNameList().get(0);
			
			//chope conv
			boolean exist = false;
			int numTab = 0;
			for(int i = 0;i<this.mFrame.getUCpane().getTabCount();i++){
				if(this.mFrame.getUCpane().getTitleAt(i) == s){
					exist = true;
					numTab = i;
				}
			}
			if(!exist){
				this.mFrame.getUCpane().AddTab(s);
				this.mFrame.getUCpane().setSelectedIndex(this.mFrame.getUCpane().getTabCount()-1);
			}else{
				this.mFrame.getUCpane().setSelectedIndex(numTab);
			}
			
			//chope onglet ou cr�e
			//change text onglet
			this.mFrame.getUCpane().getTextAreas().get(numTab).getTextArea().setText(c.toString());
		}
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
		if(ChatController.isConnected()){
			ChatController.PerformDisconnect();
		}
		
	}

	public FileTransfertConfirmation GeneratePopUp(FileTransfertDemand message) {
		// TODO Auto-generated method stub
		int n = JOptionPane.showConfirmDialog(
			    null,
			    "Would you like to accept this file from " + message.getUsername()+"\nFile name :"+message.getName()+" File Size : " + message.getSize()+"Octets",
			    "File request",
			    JOptionPane.YES_NO_OPTION);
		if(n == JOptionPane.YES_OPTION){
			return new FileTransfertConfirmation(ChatController.getLocalUsername(),true, message.getId());
		 
		}else{
			return new FileTransfertConfirmation(ChatController.getLocalUsername(),false, message.getId());

		}
		
	}
	
}
