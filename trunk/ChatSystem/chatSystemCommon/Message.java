package chatSystemCommon;

import java.io.Serializable;

public abstract class Message implements Serializable{

	private static final long serialVersionUID = 462798198653087399L;
	
	//Compteur d'id static permettant l'init de tout message avec id différent
	private static int cptId = 0; 
	private int id; 
	private String username; 
	
	public Message(String username) {
		
		this.id = Message.cptId++;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	
}