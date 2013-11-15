package chatSystemCommon;

public class FileTransfertDemand extends Message{
	
	private static final long serialVersionUID = 1347610147206375127L;
	
	private String name;
	private long size;
	
	public FileTransfertDemand(String username, String name, long size) {
		super(username);
		
		this.name = name;
		this.size = size;
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

	@Override
	public String toString() {
		return "FileTransfertDemand [id=" + id + ", username=" + username  +", name=" + name + ", size=" + size + "]";
	}
}
