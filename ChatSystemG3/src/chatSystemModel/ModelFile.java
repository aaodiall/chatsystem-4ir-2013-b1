/**
 * 
 */
package chatSystemModel;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author joanna
 *
 */
public class ModelFile extends Observable{
	
	private String remote; 
	private String name;
	private String path;
	private Long size; // en octets
	private int readMax;
	private int offset;
	private File file;
	//private File fileFromRemote;
	private int idDemand;
	private ArrayBlockingQueue<byte[]> fileParts;
	private FileInputStream reader;
	private FileOutputStream fos;
	private Boolean stateReceivedDemand;
	
	
	public ModelFile(String remote, String path){
		this.remote = remote;
		this.path = path;
		this.file = new File(this.path);
		this.name = this.file.getName();
		try {
			this.reader = new FileInputStream(this.file);
			this.size = this.file.length();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}	
	
	//constructeur reception pas terminer 
	public ModelFile(String remote, String name,long size){
		this.remote=remote;
		this.name=name;
		this.size=size;
		try {
			this.file = File.createTempFile(name, " ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//this.idDemand
		
	}
	public void buildFile(){
		this.fileParts = new ArrayBlockingQueue<byte[]> (1000000);
		this.offset = 0;
		this.readMax = 1024;
		byte[] t;
		try{
			if (this.size.intValue() < this.readMax){
				t = new byte[this.size.intValue()];
				reader.read(t);
				this.fileParts.add(t);
			}else{
				t = new byte[this.readMax];
				while ((this.offset + this.readMax) < this.size){
					reader.read(t, this.offset, t.length);
					this.fileParts.add(t);
					this.offset += this.readMax;
				}
				reader.read(t, this.offset, (this.size.intValue()-this.offset));
				this.fileParts.add(t);
			}
		} catch (FileNotFoundException e){
			e.printStackTrace();
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

	public String getName(){
		return this.name;
	}
	
	public long getSize(){
		return this.size;
	}
	
	public ArrayBlockingQueue<byte[]> getAllParts(){
		return this.fileParts;
	}
	
	public String getRemote(){
		return this.remote;
	}
	
	public int getIdDemand(){
		return this.idDemand;
	}
	
	public void setIdDemand(int id){
		this.idDemand = id;
	}
	
	public int getNumParts(){
		return this.fileParts.size();
	}
}
