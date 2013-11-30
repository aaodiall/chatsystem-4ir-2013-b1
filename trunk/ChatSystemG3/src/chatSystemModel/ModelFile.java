/**
 * 
 */
package chatSystemModel;

import java.util.Observable;

/**
 * @author joanna
 *
 */
public class ModelFile extends Observable{
	
	private String remote;
	private String name;
	private Long size; // en octets
	private int idDemand;
	private int numberOfParts;
	private int max;
	
	/**
	 * 
	 * @param remote nom de l'utilisateur distant
	 * @param idDemand numero de la demande
	 */
	public ModelFile(String remote, int idDemand, int max){
		this.remote = remote;
		this.idDemand = idDemand;
		this.max = max;
	}
	
	public void setNumberParts(){
		this.numberOfParts = this.size.intValue() / this.max;
		if ((this.size.intValue() % this.max) != 0){
			this.numberOfParts++;
		} 
	}
	
	public int getMax(){
		return this.max;
	}
	
	public int getNumberParts(){
		return this.numberOfParts;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	
	public Long getSize(){
		return this.size;
	}
	
	public void setSize(long size){
		this.size = size;
	}
	
	public int getIdDemand(){
		return this.idDemand;
	}
	
	public String getRemote(){
		return this.remote;
	}
	
}
