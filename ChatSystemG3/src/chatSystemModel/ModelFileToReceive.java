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
 * @author joanna
 *This class represents a file received by the local user
 */
public class ModelFileToReceive extends ModelFile{
	
	private File fileToReceive;
	private BufferedOutputStream bos;
	private Boolean stateReceivedDemand;
	private boolean alreadyExist;
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
		this.alreadyExist = false;
		System.out.print(System.getenv("HOME") +"/Téléchargements/"+name);
		System.out.println();
		this.fileToReceive = new File(System.getenv("HOME") +"/Téléchargements/"+name);
		try {
			this.alreadyExist = !this.fileToReceive.createNewFile();
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
	public boolean getExist(){
		return this.alreadyExist;
	}

	
	/**
	 * delete the file and close the OuputStream associated to this class 
	 * then creates a new file with the same name and a new OutputStream for this file
	 * used when the local user wants to receive a file that exists 
	 */
	public void cleanFile(){
		this.fileToReceive.delete();
		try {
			this.bos.close();
			this.fileToReceive.createNewFile();
			this.bos = new BufferedOutputStream(new FileOutputStream(this.fileToReceive,true));
		} catch (IOException e) {
			e.printStackTrace();
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