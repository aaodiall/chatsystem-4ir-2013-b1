package Controller;

import java.util.ArrayList;

public class ChatModel {
	private ArrayList<Conversation> ConversationList;
	private ArrayList<Observer> ObserverCollections;
	
	
	public ChatModel() {
		// TODO Auto-generated constructor stub
		this.ConversationList = new ArrayList<Conversation>();
		this.ObserverCollections = new ArrayList<Observer>();
		
	}
	
	public void notifyObserver(){
		
	}
	
	public void registerObserver(Observer o){
		
	}
	
	public void unregisterObserver(Observer o){
		
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
}
