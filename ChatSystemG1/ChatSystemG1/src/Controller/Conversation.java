package Controller;

import java.util.ArrayList;

public class Conversation {
	private ArrayList<SignedAndDatedMessage> messageList;
	private ArrayList<String> UserNameList;
	
	public Conversation(ArrayList<String> UserNameList) {
		// TODO Auto-generated constructor stub
		this.setUserNameList(UserNameList);
		this.setMessageList(new ArrayList<SignedAndDatedMessage>());
	}

	/**
	 * @return the userNameList
	 */
	public ArrayList<String> getUserNameList() {
		return UserNameList;
	}

	/**
	 * @param userNameList the userNameList to set
	 */
	public void setUserNameList(ArrayList<String> userNameList) {
		UserNameList = userNameList;
	}

	/**
	 * @return the messageList
	 */
	public ArrayList<SignedAndDatedMessage> getMessageList() {
		return messageList;
	}

	/**
	 * @param messageList the messageList to set
	 */
	public void setMessageList(ArrayList<SignedAndDatedMessage> messageList) {
		this.messageList = messageList;
	}
	public String toString(){
		String retour = "";
		
		for(SignedAndDatedMessage sa : this.getMessageList()){
				retour += "\n" + sa.getUserName() +" :\n " + sa.getDate() + " : \n" + sa.getText() + "\n";  
			
		}
		
		return retour;
	}
}
