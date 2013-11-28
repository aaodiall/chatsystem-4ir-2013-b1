package org.insa.java.factory;

import chatSystemCommon.FilePart;
import chatSystemCommon.FileTransfertCancel;
import chatSystemCommon.FileTransfertConfirmation;
import chatSystemCommon.FileTransfertDemand;
import chatSystemCommon.Goodbye;
import chatSystemCommon.Hello;

public class MessageFactory {
	private static Hello hello = null;
	private static Goodbye bye = null;
	private static FilePart file = null;
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
	
	public static Goodbye getByeMessage(String username) {
		if(bye == null) {
			bye = new Goodbye(username);
		}
		else {
			bye.setUsername(username);
		}
		return bye;
	}
	
	public static FilePart getFileMessage(String username, byte[] filePart, boolean isLast) {
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
	
	public static FileTransfertCancel getFileTransfertCancelMessage(String username, int idDemand) {
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
	
	public static FileTransfertDemand getFileTransfertDemandMessage(String username, String name, long size,int portClient) {
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
