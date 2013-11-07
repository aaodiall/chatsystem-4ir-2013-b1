/**
 * 
 */
package Model;

import chatSystemCommon.File;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;
import chatSystemCommon.Text;

/**
 * @author Thiebaud Thomas
 *
 */
public class MessageFactory {
	private static Hello hello = null;
	private static Text text = null;
	private static Goodbye bye = null;
	private static File file = null;
	private static FileTransfertCancel fileTransfertCancel = null;
	private static FileTransfertConfirmation fileTransfertConfirmation = null;
	private static FileTransfertDemand fileTransfertDemand = null;
	
	public static Hello getHelloMessage(String username,boolean isAck) {
		if(hello == null) {
			hello = new Hello(username, isAck);
		}
		else {
			hello.setUsername(username);
			hello.setIsAck(isAck);
		}
		return hello;
	}
	
	public static Text getTextMessage(String username, String txt) {
		if(text == null) {
			text = new Text(username, txt);
		}
		else {
			text.setUsername(username);
			text.setText(txt);
		}
		return text;
	}
	
	public static Goodbye getByeMessage(String username) {
		if(bye == null) {
			bye = new Goodbye(username);
		}
		else {
			bye.setUsername(username);
		}
		return bye;
	}
	
	public static File getFileMessage(String username, byte[] filePart, boolean isLast) {
		if(file == null) {
			file = new File(username,filePart,isLast);
		}
		else {
			file.setUsername(username);
			file.setFilePart(filePart);
			file.setIsLast(isLast);
		}
		return file;
	}
	
	public static FileTransfertCancel getFileTransfertMessage(String username, int idDemand) {
		if(fileTransfertCancel == null) {
			fileTransfertCancel = new FileTransfertCancel(username,idDemand);
		}
		else {
			fileTransfertCancel.setUsername(username);
			fileTransfertCancel.setIdDemand(idDemand);
		}
		return fileTransfertCancel;
	}
	
	public static FileTransfertConfirmation getFileTransfertConfirmationMessage(String username, boolean isAccepted, int idDemand) {
		if(fileTransfertConfirmation == null) {
			fileTransfertConfirmation = new FileTransfertConfirmation(username,isAccepted,idDemand);
		}
		else {
			fileTransfertConfirmation.setUsername(username);
			fileTransfertConfirmation.setIsAccepted(isAccepted);
			fileTransfertConfirmation.setIdDemand(idDemand);
		}
		return fileTransfertConfirmation;
	}
	
	public static FileTransfertDemand getFileTransfertDemandMessage(String username, String name, String extension, int size) {
		if(fileTransfertDemand == null) {
			fileTransfertDemand = new FileTransfertDemand(username,name,extension,size);
		}
		else {
			fileTransfertDemand.setUsername(username);
			fileTransfertDemand.setName(name);
			fileTransfertDemand.setExtension(extension);
			fileTransfertDemand.setSize(size);
		}
		return fileTransfertDemand;
	}
}
