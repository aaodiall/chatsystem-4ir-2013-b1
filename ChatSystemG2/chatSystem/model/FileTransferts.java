package chatSystem.model;

import java.util.ArrayList;
import java.util.List;

public class FileTransferts {
	private static FileTransferts instance;
	private List<FileTransfertInformation> FileModel;

	private FileTransferts() {
		this.FileModel = new ArrayList<FileTransfertInformation>();
	}
	
	public void addTransfert(String name, String extension, int taille, String idRemoteSystem, int idTransfert) {
		this.FileModel.add(new FileTransfertInformation(taille,idRemoteSystem, name, extension, idTransfert));
	}
	
	public void deleteTransfert(int idTransfert) {
	}
	
	public void getFileTransfertInformation() {
	}
	
	public static FileTransferts getInstance() {
		if (instance == null) {
			instance = new FileTransferts();
		}
		return instance;
	}
}
