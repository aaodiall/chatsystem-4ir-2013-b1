package Controller;

import java.util.ArrayList;


public class ChatModel {
    private static final int NOTIF_USERLIST = 0;
    private static final int NOTIF_CONV = 1;
    
    
    
    
	private ArrayList<Conversation> ConversationList;

	

	private ArrayList<Observer> ObserverCollections;
	private ArrayList<String> UserNameList;
	
	
	public ChatModel() {
		// TODO Auto-generated constructor stub
		this.ConversationList = new ArrayList<Conversation>();
		this.ObserverCollections = new ArrayList<Observer>();
		this.UserNameList = new ArrayList<String>();
		
		
	}
	
	public void notifyObserver(int notif){
		for(Observer o : getObserverCollections()){
			o.Notify(notif);
		}
		
	}
	
	public void registerObserver(Observer o){
		getObserverCollections().add(o);
	}
	
	public void unregisterObserver(Observer o){
		getObserverCollections().remove(o);
	}

	/**
	 * @return the conversationList
	 */
	public ArrayList<Conversation> getConversationList() {
		return ConversationList;
	}

	/**
	 * @param conversationList the conversationList to set
	 */
	public void setConversationList(ArrayList<Conversation> conversationList) {
		ConversationList = conversationList;
	}

	/**
	 * @return the observerCollections
	 */
	public ArrayList<Observer> getObserverCollections() {
		return ObserverCollections;
	}

	/**
	 * @param observerCollections the observerCollections to set
	 */
	public void setObserverCollections(ArrayList<Observer> observerCollections) {
		ObserverCollections = observerCollections;
	}
	public ArrayList<String> getUserNameList() {
		return UserNameList;
	}

	public void addUser(String username) {
		// TODO Auto-generated method stub
		UserNameList.add(username);
		notifyObserver(NOTIF_USERLIST);
		
	}
}
