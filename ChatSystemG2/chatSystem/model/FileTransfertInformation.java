package chatSystem.model;

public class FileTransfertInformation extends Model{
	
	private int taille;
	private String idRemoteSystem;
	private FileState state;
	private String name;
	private String path;
	private String extension;
	private int idTransfert;
	private int tailleRecup;
	
	public FileTransfertInformation(int taille, String idRemoteSystem, String name, String extension, int idTransfert) {
		this.taille = taille;
		this.idRemoteSystem = idRemoteSystem;
		this.name = name;
		this.extension = extension;
		this.idTransfert = idTransfert;
		
		this.state = FileState.WAITANSWER;
		this.tailleRecup = 0;
		this.path = null;
	}
	
	public FileState getState() {
		return this.state;
	}
	
	public int getProgression() {
		return 0;
	}
	
	public String getFilePart() {
		return "";
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setState(FileState state) {
		this.state = state;
	}
	
	public void addFilePart() {		
	}
}
