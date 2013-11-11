package Controller;

import java.util.Date;

public class SignedAndDatedMessage {
	private Date date;
	private String text;
	private String userName;
	
	public SignedAndDatedMessage(Date d,String t,String u) {
		// TODO Auto-generated constructor stub
		this.date = d;
		this.text = t;
		this.userName = u;
				
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
