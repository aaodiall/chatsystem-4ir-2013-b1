package chatSystemCommon;

public class FileTransfertDemand extends Message{
	
	private static final long serialVersionUID = 1347610147206375127L;
	
	private String name;
	private long size;
	private int portClient;
        private final int idDemand;
	
	public FileTransfertDemand(String username, String name, long size, int portClient, int idDemand) {
		super(username);
		
		this.name = name;
		this.size = size;
		this.portClient = portClient;
                this.idDemand = idDemand;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}

	public int getPortClient() {
		return portClient;
	}

	public void setPortClient(int portClient) {
		this.portClient = portClient;
	}

	@Override
	public String toString() {
		return "FileTransfertDemand [name=" + name + ", size=" + size+ ", portClient=" + portClient + ", idDemand=" + idDemand + "]";
	}
}
