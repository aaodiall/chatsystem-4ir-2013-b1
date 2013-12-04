/**
 * 
 */
package chatSystemModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author joanna
 *
 */
public class ModelFileToSend extends ModelFile{

	private String path;
	private BufferedInputStream reader;
	private File fileToSend;
	private boolean fileTransmitted;
	private byte[] partbytes;
	
	private Integer progression;
	
	public ModelFileToSend(String remote, String path, int idDemand, int maxRead){
		super(remote, idDemand, maxRead);
		this.progression=new Integer(0);
		this.path = path;
		this.fileToSend = new File(this.path);
		this.fileTransmitted = false;
		this.partbytes = new byte[super.getMax()];
		try {
			this.reader = new BufferedInputStream(new FileInputStream(this.fileToSend));
			super.setName(this.fileToSend.getName());
			super.setSize(this.fileToSend.length());
			super.setNumberParts();
			System.out.println("number parts = "+ super.getNumberParts());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public byte[] readNextPart(){
		int level=0;
		int i;
		int nbBytesRead = 0;
		byte[] swap;
		try{
			nbBytesRead = reader.read(this.partbytes);
			if (nbBytesRead < 0){
				this.setSent();
				this.reader.close();
				return new byte[0];
			}else if (nbBytesRead < super.getMax()){
				level++;
				this.setProgress(level);
				swap = new byte[nbBytesRead];
				for (i=0;i<nbBytesRead;i++){
					swap[i] = this.partbytes[i];
				}
				return swap;
			}else{
				level++;
				this.setProgress(level); 
				return this.partbytes; 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setProgress(int level){
		this.progression+=(Integer)(100*level/(super.getNumberParts()));
		//System.out.println("dans setProgress de modelFile "+ this.getRemote()+" progression"+this.progression);
		setChanged();
		notifyObservers();
		//System.out.println("dans setProgress de modelFile apres notify"+ this.getRemote());
	}
	
	public Integer getProgression() {
		return progression;
	}
	
	/*public void resetProgress(){
		this.progression=0;
	}*/
	
	public void setSent(){
		this.fileTransmitted = true;
		this.setChanged();
		this.notifyObservers(this.fileTransmitted);
	}
	
}