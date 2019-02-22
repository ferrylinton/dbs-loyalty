package com.dbs.priviledge.exception;

public class NotFoundException extends AbstractException {

	private static final long serialVersionUID = 1L;
	
	public NotFoundException() {
        super("message.dataNotFound");
    }

	@Override
	public int getStatus() {
		return 404;
	}
	
}
