package com.dbs.loyalty.exception;

import com.dbs.loyalty.web.response.BadRequestResponse;

public class BadRequestException extends AbstractException {

	private static final long serialVersionUID = 1L;
	
	private BadRequestResponse response;
	
	public BadRequestException(BadRequestResponse response) {
        super("BadRequest");
        this.response = response;
    }
	
	public BadRequestException(String message) {
        super(message);
        this.response = new BadRequestResponse(message);
    }

	@Override
	public int getStatus() {
		return 400;
	}
	
	public BadRequestResponse getResponse() {
		return response;
	}
}
