package chatSystemCommon;

/*
 * This class may not be used for now, here just in case
 */
public class FileTransfertCancel extends Message {
	
	private static final long serialVersionUID = 7715571116132980646L;
	
	private int idDemand;
	
	public FileTransfertCancel(String username, int idDemand) {
		super(username);
		this.idDemand = idDemand;
	}

	public int getIdDemand() {
		return idDemand;
	}
	

}
