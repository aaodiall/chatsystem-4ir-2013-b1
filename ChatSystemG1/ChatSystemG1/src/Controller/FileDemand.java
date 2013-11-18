package Controller;

import java.io.File;

public class FileDemand {
	private int DemandId;
	/**
	 * @return the demandId
	 */
	public int getDemandId() {
		return DemandId;
	}
	/**
	 * @return the fichier
	 */
	public File getFichier() {
		return fichier;
	}
	/**
	 * @param demandId the demandId to set
	 */
	public void setDemandId(int demandId) {
		DemandId = demandId;
	}
	/**
	 * @param fichier the fichier to set
	 */
	public void setFichier(File fichier) {
		this.fichier = fichier;
	}
	private File fichier;
	
	public FileDemand(int id,File f){
		this.fichier =f;
		this.DemandId = id;
	}
}
