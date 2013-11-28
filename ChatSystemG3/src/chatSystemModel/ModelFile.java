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
	
	/**
	 * 
	 * @param remote nom de l'utilisateur distant
	 * @param idDemand numero de la demande
	 */
	public ModelFile(String remote, int idDemand){
		this.remote = remote;
		this.idDemand = idDemand;
	}
	
	public void setNumberParts(int maxTransmit){
		this.numberOfParts = this.size.intValue() / maxTransmit;
		if ((this.size.intValue() % maxTransmit) != 0){
			this.numberOfParts++;
		} 
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
