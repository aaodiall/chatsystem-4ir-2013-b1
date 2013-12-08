package org.insa.java.controller;

/**
 * Enumeration class that contains state for file emission and reception transfer.
 * @author thomas thiebaud
 * @author unaï sanchez
 */
public enum TransferState {
	AVAILABLE,
	PROCESSING,
	TERMINATED,
	CANCELED;
}
