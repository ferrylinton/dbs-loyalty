package com.dbs.loyalty.exception;

import com.dbs.loyalty.web.response.BadRequestResponse;

public class BadRequestException extends AbstractException {

	private static final long serialVersionUID = 1L;
	
	private final BadRequestResponse response;
	
	public BadRequestException(BadRequestResponse response) {
        super("BadRequest");
        this.response = response;
    }

	@Override
	public int getStatus() {
		return 400;
	}
	
	public BadRequestResponse getResponse() {
		return response;
	}
}
