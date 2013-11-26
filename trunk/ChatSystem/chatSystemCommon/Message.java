package chatSystemCommon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Message implements Serializable{

	private static final long serialVersionUID = 462798198653087399L;
	
	//Compteur d'id static permettant l'init de tout message avec id différent
	protected static int cptId = 0; 
	protected int id; 
	protected String username; 
	
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
	
	public void setUsername(String username) {
		this.username = username;
	}
        
    public byte[] toArray() throws IOException{
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(output);
        
        oout.writeObject(this);
        
        byte[] retour = output.toByteArray();
        
        oout.close();
        output.close();
        
        return retour;
    }
    
    public static Message fromArray(byte[] array) throws IOException{
        ByteArrayInputStream input = new ByteArrayInputStream(array);
        ObjectInputStream oiut = new ObjectInputStream(input);
        
        Message retour = null;
        try {
            retour = (Message)oiut.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
        	input.close();
        	oiut.close();
        }
        
        return retour;
    }

	@Override
	public String toString() {
		return "Message [id=" + id + ", username=" + username + "]";
	}   
}