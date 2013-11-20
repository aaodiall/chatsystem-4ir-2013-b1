package chatSystem.model;

public class FileTransfertInformation extends Model{
	
	private long taille;
	private String idRemoteSystem;
	private FileState state;
	private String name;
	private String path;
	private int idTransfert;
	private int tailleRecup;
	
        /**
         * Class' constructor
         * @param taille file's size
         * @param idRemoteSystem id of the sending remote system
         * @param name file's name
         * @param idTransfert id given to the transfer
         */
	public FileTransfertInformation(long taille, String idRemoteSystem, String name, int idTransfert) {
		this.taille = taille;
		this.idRemoteSystem = idRemoteSystem;
		this.name = name;
		this.idTransfert = idTransfert;	
		this.state = FileState.WAITANSWER;
		this.tailleRecup = 0;
		this.path = null;
	}
	
        /**
         * Obtaining the file's state
         * @return file's state
         */
	public FileState getState() {
		return this.state;
	}
	
        /**
         * Obtaining where the system is in the file's tranfer
         * @return transfer's progression
         */
	public int getProgression() {
		return 0;
	}
	
        /**
         * Obtaining the next file's part to send
         * @return file's part
         */
	public byte[] getFilePart() {
            //juste pour que ça compile mais en fait je sais pas quoi faire :p
            //et oui je suis fière de mon implémentation :p
		return "".getBytes();
	}
	
        /**
         * Obtaining the file's path in the system
         * @return file's path
         */
	public String getPath() {
		return this.path;
	}
	
        /**
         * Setting the file's path in the system
         * @param path new file's path
         */
	public void setPath(String path) {
		this.path = path;
	}
	
        /**
         * Setting the sending state of the file
         * @param state new file's state
         */
	public void setState(FileState state) {
		this.state = state;
	}
	
        /**
         * Add a new file part
         */
	public void addFilePart() {		
	}
}
