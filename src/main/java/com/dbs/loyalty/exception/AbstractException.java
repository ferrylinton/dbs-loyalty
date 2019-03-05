package com.dbs.loyalty.exception;

public abstract class AbstractException extends Exception {

	private static final long serialVersionUID = 1L;

	public AbstractException(String message) {
        super(message);
    }

	public abstract int getStatus();
	
}
