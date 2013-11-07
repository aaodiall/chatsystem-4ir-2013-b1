package chatSystemCommon;

public class Text extends Message{
	
	private static final long serialVersionUID = -1548242279095457032L;
	
	private String text;
	
	public Text(String username, String text) {
		super(username);
		
		this.text = text;
	}

	public String getText() {
		return text;
	}

}