package com.dbs.loyalty.model;

import io.swagger.annotations.ApiModelProperty;

public class ErrorResponse {

	@ApiModelProperty(value = "Error's message", example = "Wrong Email or Password")
	private String message;

	public ErrorResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
