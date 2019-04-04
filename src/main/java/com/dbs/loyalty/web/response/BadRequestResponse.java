package com.dbs.loyalty.web.response;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadRequestResponse extends AbstractResponse{

	private static final long serialVersionUID = 1L;
	
	private List<String> fields;

	public BadRequestResponse(String message) {
		super(message);
	}
	
	public BadRequestResponse(String message, String field) {
		super(message);
		fields = Arrays.asList(field);
	}
	
	public BadRequestResponse(String message, List<String> fields) {
		super(message);
		this.fields = fields;
	}
	
}
