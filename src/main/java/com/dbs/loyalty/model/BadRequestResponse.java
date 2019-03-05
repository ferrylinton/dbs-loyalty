package com.dbs.loyalty.model;

import java.util.ArrayList;
import java.util.List;

public class BadRequestResponse {

	private String message;
	
	private List<String> fields = new ArrayList<>();

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	
}
