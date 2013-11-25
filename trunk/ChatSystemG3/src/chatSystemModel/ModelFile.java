/**
 * 
 */
package chatSystemModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author joanna
 *
 */
public class ModelFile {
	
	private String remote; 
	private String name;
	private String path;
	private Long size;
	private int readMax;
	private int offset;
	private File fileToSend;
	private int idDemand;
	private ArrayBlockingQueue<byte[]> fileParts;
	private FileInputStream reader;
	private Boolean stateReceivedDemand;
	
	
	public ModelFile(String remote, String path){
		this.remote = remote;
		this.path = path;
		this.fileToSend = new File(this.path);
		this.name = this.fileToSend.getName();
		try {
			this.reader = new FileInputStream(this.fileToSend);
			this.size = this.fileToSend.getTotalSpace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void buildFile(){
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
