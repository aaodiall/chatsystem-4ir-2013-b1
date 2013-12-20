/**
 * 
 */
package chatSystemModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class represents a file received by the local user
 * @author joanna
 *
 */
public class ModelFileToReceive extends ModelFile{
	
	private File fileToReceive;
	private BufferedOutputStream bos;
	private Boolean stateReceivedDemand;
	private boolean created;
	/**
	 * ModelFileToReceive constructor
	 * si le fichier existe déjà et que l'utilisateur accepte de le recevoir on ecrase le précédent contenu
	 * @param remote
	 * @param name
	 * @param size
	 * @param idDemand
	 * @param maxWrite
	 */
	public ModelFileToReceive(String remote, String name,long size, int idDemand,int maxWrite){
		super(remote,idDemand, maxWrite);
		super.setName(name);
		super.setSize(size);
		super.setNumberParts();
		this.created = false;
		this.fileToReceive = new File(System.getenv("HOME") +"/Téléchargements/"+name);
		try {
			this.created = this.fileToReceive.createNewFile();
			this.bos = new BufferedOutputStream(new FileOutputStream(this.fileToReceive,true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * indicates if the file to receive already exists
	 * @return yes or no
	 */
	public boolean hasBeenCreated(){
		return this.created;
	}

	
	/**
	 * delete the file and close the OuputStream associated to this class 
	 * then creates a new file with the same name and a new OutputStream for this file
	 * used when the local user wants to receive a file that exists 
	 */
	public void cleanFile(){
		int i=1;
		// recherche d'un nom qui convient
		while(this.created && i<6){
			this.fileToReceive = new File(System.getenv("HOME") +"/Téléchargements/"+this.getName()+"("+i+")");
			try {
				this.created = !this.fileToReceive.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
		if(i==6){ this.fileToReceive.delete();
		}else{
			try {
				this.bos.close();
				this.bos = new BufferedOutputStream(new FileOutputStream(this.fileToReceive,true));
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * write a part of file in memory
	 * @param part
	 * @param isReceived
	 */
	public void writeFilePart(byte[] part, boolean isReceived){
		try {
			this.bos.write(part);
			this.bos.flush();
			this.levelUP();
			this.setProgress(this.getLevel());
			if (isReceived== true){
				this.bos.close();
				this.resetLevel();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void setProgress(int level){
		this.calcProgress();
		setChanged();
		notifyObservers(this);
	}

	/**
	 * delete the file associated
	 */
	public void deleteFile(){
		this.fileToReceive.delete();
	}
	
	/**
	 * indicates if a demand has been received
	 * @return yes or no
	 */
	public Boolean getStateReceivedDemand() {
		return stateReceivedDemand;
	}

	/**
	 * change the state of the demand
	 * use to notify the Gui that a demand has been received
	 * @param stateReceivedDemand
	 */
	public void setStateReceivedDemand(Boolean stateReceivedDemand) {
		this.stateReceivedDemand = stateReceivedDemand;
		this.setChanged();
		this.notifyObservers(this);
		this.stateReceivedDemand = false;
	}
}