package org.insa.java.factory;

import chatSystemCommon.FilePart;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;

/**
 * Factory that is able to create all the type of messages.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public class MessageFactory {
	private static Hello hello = null;
	private static Goodbye bye = null;
	private static FilePart file = null;
	private static FileTransfertCancel fileTransfertCancel = null;
	private static FileTransfertConfirmation fileTransfertConfirmation = null;
	private static FileTransfertDemand fileTransfertDemand = null;
	
	/**
	 * Create a hello message.
	 * @param username Local user name.
	 * @param isAck False hello message, true answer to a hello message.
	 * @return helloMessage HelloMessage created.
	 */
	public static Hello hello(String username,boolean isAck) {
		if(hello == null) {
			hello = new Hello(username, isAck);
		}
		else {
			hello.setUsername(username);
			hello.setIsAck(isAck);
		}
		return hello;
	}
	
	/**
	 * Create a bye message.
	 * @param username Local user name.
	 * @return byeMessage ByeMessage created.
	 */
	public static Goodbye bye(String username) {
		if(bye == null) {
			bye = new Goodbye(username);
		}
		else {
			bye.setUsername(username);
		}
		return bye;
	}
	
	/**
	 * Create a file message.
	 * @param username Local user name.
	 * @param filePart A part of a file.
	 * @param isLast True if filePart is the last part of the file.
	 * @return filePart FilePart message created.
	 */
	public static FilePart file(String username, byte[] filePart, boolean isLast) {
		if(file == null) {
			file = new FilePart(username,filePart,isLast);
		}
		else {
			file.setUsername(username);
			file.setFilePart(filePart);
			file.setIsLast(isLast);
		}
		return file;
	}
	
	/**
	 * Create a cancel message.
	 * @param username Local user name.
	 * @param idDemand The id corresponding to the transfer you want to cancel.
	 * @return fileTransfertCancel FileTransfertCancel message created.
	 */
	public static FileTransfertCancel cancel(String username, int idDemand) {
		if(fileTransfertCancel == null) {
			fileTransfertCancel = new FileTransfertCancel(username,idDemand);
		}
		else {
			fileTransfertCancel.setUsername(username);
			fileTransfertCancel.setIdDemand(idDemand);
		}
		return fileTransfertCancel;
	}
	
	/**
	 * Create a confirmation message.
	 * @param username Local user name.
	 * @param isAccepted True if you accept the file transfer, false otherwise.
	 * @param idDemand The id corresponding to the transfer you want to accept or refuse.
	 * @return fileTransfertConfirmation FileTransfertConfirmation message created.
	 */
	public static FileTransfertConfirmation confirmation(String username, boolean isAccepted, int idDemand) {
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
	
	/**
	 * Create a demand message
	 * @param username Local user name.
	 * @param name File name.
	 * @param size File size.
	 * @param portClient TCP port for transfer.
	 * @return fileTransfertDemand FileTransfertDemand message created
	 */
	public static FileTransfertDemand demand(String username, String name, long size,int portClient) {
		if(fileTransfertDemand == null) {
			fileTransfertDemand = new FileTransfertDemand(username,name,size,portClient,1);
		}
		else {
			fileTransfertDemand.setUsername(username);
			fileTransfertDemand.setName(name);
			fileTransfertDemand.setSize(size);
			fileTransfertDemand.setPortClient(portClient);
		}
		return fileTransfertDemand;
	}
}
