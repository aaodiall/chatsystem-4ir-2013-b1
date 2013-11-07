package chatSystemCommon;

public class FileTransfertConfirmation extends Message{
	
	private static final long serialVersionUID = -5827489411844610984L;
	
	private boolean isAccepted;
	private int idDemand;
	
	public FileTransfertConfirmation(String username, boolean isAccepted, int idDemand) {
		super(username);
		
		this.isAccepted = isAccepted;
		this.idDemand = idDemand;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public int getIdDemand() {
		return idDemand;
	}

	@Override
	public String toString() {
		return "FileTransfertConfirmation [id=" + id + ", username=" + username  +", isAccepted=" + isAccepted + ", idDemand=" + idDemand + "]";
	}
}
