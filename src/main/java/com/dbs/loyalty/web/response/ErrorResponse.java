package com.dbs.loyalty.web.response;

import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse extends Response{

	private static final long serialVersionUID = 1L;
	
	private String detail;

	public ErrorResponse(String message) {
		super(message);
	}
	
	public ErrorResponse(Exception ex) {
		super(ex.getLocalizedMessage());
		this.detail = ExceptionUtils.getStackTrace(ex);
	}
	
}
