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
public class ModelFileToSend extends ModelFile{

	private String path;
	private int readMax;
	private int offset;
	private FileInputStream reader;
	private File fileToSend;
	private ArrayBlockingQueue<byte[]> fileParts;
	
	public ModelFileToSend(String remote, String path, int idDemand){
		super(remote, idDemand);
		this.path = path;
		this.fileToSend = new File(this.path);
		try {
			this.reader = new FileInputStream(this.fileToSend);
			super.setName(this.fileToSend.getName());
			super.setSize(this.fileToSend.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void readFile(){
		this.readMax = 1024;
		int numberOfParts = super.getSize().intValue() / this.readMax;
		if ( super.getSize().intValue() % this.readMax != 0){
			numberOfParts++;
		}
		this.fileParts = new ArrayBlockingQueue<byte[]> (numberOfParts);
		this.offset = 0;
		byte[] t;
		try{
			if (super.getSize().intValue() <= this.readMax){
				t = new byte[super.getSize().intValue()];
				reader.read(t);
				this.fileParts.add(t);
			}else{
				t = new byte[this.readMax];
				while ((this.offset + this.readMax) < super.getSize()){
					reader.read(t, this.offset, t.length);
					this.fileParts.add(t);
					this.offset += this.readMax;
				}
				reader.read(t, this.offset, (super.getSize().intValue()-this.offset));
				this.fileParts.add(t);
			}
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("number parts = "+this.fileParts.size());
	}
	
	public int getNumParts(){
		return this.fileParts.size();
	}
	
	public ArrayBlockingQueue<byte[]> getAllParts(){
		return this.fileParts;
	}
	
}
