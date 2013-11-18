package Controller;

import java.io.File;

public class FileDemand {
	private int DemandId;
	private int DemandPort;
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
	 * @return the demandPort
	 */
	public int getDemandPort() {
		return DemandPort;
	}
	/**
	 * @param demandPort the demandPort to set
	 */
	public void setDemandPort(int demandPort) {
		DemandPort = demandPort;
	}
	/**
	 * @param fichier the fichier to set
	 */
	public void setFichier(File fichier) {
		this.fichier = fichier;
	}
	private File fichier;
	
	public FileDemand(int id,File f,int port){
		this.fichier =f;
		this.DemandId = id;
		this.DemandPort = port;
	}
}
