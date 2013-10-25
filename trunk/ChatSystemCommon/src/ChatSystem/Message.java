package ChatSystem;

import java.io.Serializable;

public abstract class Message implements Serializable{
	private static int cptId = 0; //Compteur d'id static permettant l'init de tout message avec id différent
	private int id; 
	private String UserName; 
	public Message(String UName) {
		// TODO Auto-generated constructor stub
		id = cptId;
		cptId ++;
		UserName = UName;
	}
	
}
