package org.insa.java.view;

/**
 * Generic network interface for java-based application.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public abstract class JavaChatNI implements Runnable{
	protected final int UDP_CLIENT_PORT = 16000;
	protected final int UDP_SERVER_PORT = 16001;
	protected final String UDP_BROADCAST_EMISSION = "255.255.255.255";
	protected int TCP_CLIENT_PORT = 12345;
}