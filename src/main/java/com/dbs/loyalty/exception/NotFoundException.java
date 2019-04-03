package com.dbs.loyalty.exception;

public class NotFoundException extends AbstractException {

	private static final long serialVersionUID = 1L;
	
	public NotFoundException() {
        super("message.dataNotFound");
    }
	
	public NotFoundException(String message) {
        super(message);
    }

	@Override
	public int getStatus() {
		return 404;
	}
	
}
