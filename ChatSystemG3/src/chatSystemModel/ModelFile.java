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
	private Boolean fileReceived;
	private Integer fileSent;
	

	/**
	 * 
	 * @param remote nom de l'utilisateur distant
	 * @param idDemand numero de la demande
	 */
	public ModelFile(){
		this.remote = new String();
		this.idDemand =0;
		this.max =1;
	}
	
	

	public void setRemote(String remote) {
		this.remote = remote;
	}



	public void setIdDemand(int idDemand) {
		this.idDemand = idDemand;
	}



	public void setMax(int max) {
		this.max = max;
	}



	public void setNumberParts(){
		this.numberOfParts = this.size.intValue() / this.max;
		if ((this.size.intValue() % this.max) != 0){
			this.numberOfParts++;
		} 
	}
	public void setFileReceived(Boolean fileReceived,String remote) {
		this.fileReceived = fileReceived;
		System.out.println("dans setFileReceived de modelFile"+ this.getRemote());
		setChanged();
		notifyObservers(remote);
		
	}
	public void setFileSent(Integer fileSent ,String remote) {
		this.fileSent = fileSent;
		System.out.println("dans setFileReceived de modelFile"+ this.getRemote());
		setChanged();
		notifyObservers(remote);
		
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
