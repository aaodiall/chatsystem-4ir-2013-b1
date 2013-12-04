/**
 * 
 */
package chatSystemModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author joanna
 *
 */
public class ModelFileToReceive extends ModelFile{
	
	private File fileToReceive;
	private FileOutputStream fos;
	private Boolean stateReceivedDemand;
	private Boolean isReceived=false;
	//private Integer progression;
	
	public ModelFileToReceive(String remote, String name,long size, int idDemand,int maxWrite){
		super(remote,idDemand, maxWrite);
		//super.setRemote(remote);
		//System.out.println(getRemote()+"Dans modelFilereceiver constructeur");
		//super.setSize(maxWrite);
		//super.setIdDemand(idDemand);
		super.setName(name);
		super.setSize(size);
		super.setNumberParts();
		
		System.out.print(System.getenv("HOME") +"/Téléchargements/"+name);
		System.out.println(" ");
		this.fileToReceive = new File(System.getenv("HOME") +"/Téléchargements/"+name);
		try {
			this.fileToReceive.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFilePart(byte[] part, boolean isReceived){
		try {
			this.fos = new FileOutputStream(this.fileToReceive,true);
			this.fos.write(part);
			this.fos.flush();
			
			if (isReceived== true){
				this.setIsReceived();
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void setIsReceived(){
		this.isReceived=true;
		setChanged();
		notifyObservers();
		System.out.println("dans setIsReceve");
		try {
			this.fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Boolean getStateReceivedDemand() {
		return stateReceivedDemand;
	}

	public void setStateReceivedDemand(Boolean stateReceivedDemand) {
		this.stateReceivedDemand = stateReceivedDemand;
		
	}

	/*public void setProgression(){
		this.progression = this.progression/super.getNumberParts();
		setChanged();
		notifyObservers(progression);
	}*/
	
}
