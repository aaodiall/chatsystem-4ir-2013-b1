package chatSystemCommon;

public class FileTransfertDemand extends Message{
	
	private static final long serialVersionUID = 1347610147206375127L;
	
	private String name;
	private String extension;
	private int size;
	
	public FileTransfertDemand(String username, String name, String extension, int size) {
		super(username);
		
		this.name = name;
		this.extension = extension;
		this.size = size;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "FileTransfertDemand [id=" + id + ", username=" + username  +", name=" + name + ", extension=" + extension + ", size=" + size + "]";
	}
}
