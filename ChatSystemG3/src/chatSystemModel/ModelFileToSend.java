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
	private FileInputStream reader;
	private File fileToSend;
	private ArrayBlockingQueue<byte[]> fileParts;
	
	public ModelFileToSend(String remote, String path, int idDemand, int maxRead){
		super(remote, idDemand, maxRead);
		this.path = path;
		this.fileToSend = new File(this.path);
		try {
			this.reader = new FileInputStream(this.fileToSend);
			super.setName(this.fileToSend.getName());
			super.setSize(this.fileToSend.length());
			super.setNumberParts();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void readFile(){
		super.setNumberParts();
		System.out.println("number parts = "+ super.getNumberParts());
		this.fileParts = new ArrayBlockingQueue<byte[]> (super.getNumberParts());
		int i;
		int nbBytesRead = 0;
		byte[] t;
		byte[] swap;
		try{
			if (super.getSize().intValue() <= super.getMax()){
				t = new byte[super.getSize().intValue()];
				reader.read(t);
				this.fileParts.add(t);
			}else{
				while (nbBytesRead >= 0){
					t = new byte[super.getMax()];
					nbBytesRead = reader.read(t);
					System.out.println( "size file part " +nbBytesRead);
					if ((nbBytesRead < super.getMax()) && (nbBytesRead > 0)){
						swap = new byte[nbBytesRead];
						for (i=0;i<nbBytesRead;i++){
							swap[i] = t[i];
						}
						this.fileParts.add(swap);
					}else if (nbBytesRead == super.getMax()){
						this.fileParts.add(t); 
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public int getNumParts(){
		return this.fileParts.size();
	}
	
	public ArrayBlockingQueue<byte[]> getAllParts(){
		return this.fileParts;
	}
	
}
