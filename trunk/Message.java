
public abstract class Message {
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
