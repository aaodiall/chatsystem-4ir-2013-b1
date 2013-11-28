/**
 * 
 */
package chatSystemModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author joanna
 *
 */
public class ModelFileToReceive extends ModelFile{
	
	private File fileToReceive;
	private FileOutputStream fos;
	private Boolean stateReceivedDemand;
	private Boolean isReceived;
	private int numberOfParts;
	private int progression;
	
	public ModelFileToReceive(String remote, String name,long size, int idDemand){
		super(remote,idDemand);
		super.setName(name);
		super.setSize(size);
		this.progression = 0;
		System.out.print(System.getenv("HOME") +"/Téléchargements/"+name);
		System.out.println(" ");
		this.fileToReceive = new File(System.getenv("HOME") +"/Téléchargements/"+name);
		try {
			this.fileToReceive.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFilePart(byte[] part, boolean isLast){
		try {
			System.out.println("writing file...");
			this.fos = new FileOutputStream(this.fileToReceive);
			this.fos.write(part);
			System.out.println("part written");
			this.fos.flush();
			this.progression++;
			this.setProgression();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void setIsReceived(){
		this.isReceived = true;
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
		setChanged();
		notifyObservers(this.stateReceivedDemand);
	}

	public void setNumberOfParts(int n){
		this.numberOfParts = n;
		this.setProgression();
	}

	public void setProgression(){
		this.progression = this.progression/this.numberOfParts;
	}
	
}
