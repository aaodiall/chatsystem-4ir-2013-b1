package Controller;

import java.io.File;
import java.util.ArrayList;


public class ChatModel {
    private static final int NOTIF_USERLIST = 0;
    
    
    
    
    
	private ArrayList<Conversation> ConversationList;
	private ArrayList<FileDemand> FileDemandList;
	

	private ArrayList<Observer> ObserverCollections;
	private ArrayList<String> UserNameList;
	
	
	public ChatModel() {
		// TODO Auto-generated constructor stub
		this.ConversationList = new ArrayList<Conversation>();
		this.ObserverCollections = new ArrayList<Observer>();
		this.FileDemandList = new ArrayList<FileDemand>();
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

	public void RemoveUser(String username) {
		// TODO Auto-generated method stub
		UserNameList.remove(username);
		notifyObserver(NOTIF_USERLIST);
	}

	/**
	 * @return the fileDemandList
	 */
	public ArrayList<FileDemand> getFileDemandList() {
		return FileDemandList;
	}

	/**
	 * @param fileDemandList the fileDemandList to set
	 */
	public void setFileDemandList(ArrayList<FileDemand> fileDemandList) {
		FileDemandList = fileDemandList;
	}

	public File getById(int idDemand) {
		// TODO Auto-generated method stub
		for(FileDemand fd : this.FileDemandList){
			if(fd.getDemandId() == idDemand){
				return fd.getFichier();
			}
		}
		return null;
	}

	public void removeByID(int idDemand) {
		// TODO Auto-generated method stub
		FileDemand toremove = null;
		for(FileDemand fd : this.FileDemandList){
			if(fd.getDemandId() == idDemand){
				toremove = fd;
			}
		}
		FileDemandList.remove(toremove);
	}

	public int getPortByID(int idDemand) {
		// TODO Auto-generated method stub
		for(FileDemand fd : this.FileDemandList){
			if(fd.getDemandId() == idDemand){
				return fd.getDemandPort();
			}
		}
		return 0;
	}
}
