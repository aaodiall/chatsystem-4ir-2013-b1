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
 * this class represents a file that will be sent
 * @author joanna
 * 
 */
public class ModelFileToSend extends ModelFile{

	private String path;
	private BufferedInputStream reader;
	private File fileToSend;
	private byte[] partbytes;
	boolean fileRefused;
	
	public ModelFileToSend(String remote, String path, int idDemand, int maxRead){
		super(remote, idDemand, maxRead);
		this.path = path;
		this.fileToSend = new File(this.path);
		this.partbytes = new byte[super.getMax()];
		this.fileRefused = false;
		try {
			this.reader = new BufferedInputStream(new FileInputStream(this.fileToSend));
			super.setName(this.fileToSend.getName());
			super.setSize(this.fileToSend.length());
			super.setNumberParts();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public byte[] readNextPart(){
		int i;
		int nbBytesRead = 0;
		byte[] swap;
		try{
			nbBytesRead = reader.read(this.partbytes);
			if (nbBytesRead < 0){
				this.reader.close();
				return new byte[0];
			}else if (nbBytesRead < super.getMax()){
				this.levelUP();
				this.setProgress(this.getLevel());
				swap = new byte[nbBytesRead];
				for (i=0;i<nbBytesRead;i++){
					swap[i] = this.partbytes[i];
				}
				return swap;
			}else{
				this.levelUP();
				this.setProgress(this.getLevel()); 
				return this.partbytes; 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void setProgress(int level){
		this.calcProgress();
		setChanged();
		notifyObservers(this);
	}
	
	public boolean isRefused(){	return this.fileRefused; }
	
	public void setRefused(){
		this.fileRefused = true;
		this.setChanged();
		this.notifyObservers(this);
	}
}