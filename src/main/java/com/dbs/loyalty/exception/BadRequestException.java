package com.dbs.loyalty.exception;

public class BadRequestException extends AbstractException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
        super(message);
    }

	@Override
	public int getStatus() {
		return 400;
	}

}
