package chatSystemCommon;

public class FileTransfertDemand extends Message{
	
	private static final long serialVersionUID = 1347610147206375127L;
	
	private String nom;
	private String extension;
	private int taille;
	
	public FileTransfertDemand(String username, String nom, String extension, int taille) {
		super(username);
		
		this.nom = nom;
		this.extension = extension;
		this.taille = taille;
	}

	public String getNom() {
		return nom;
	}

	public String getExtension() {
		return extension;
	}

	public int getTaille() {
		return taille;
	}

	@Override
	public String toString() {
		return "FileTransfertDemand [id=" + id + ", username=" + username  +",nom=" + nom + ", extension=" + extension + ", taille=" + taille + "]";
	}
}
