package com.dbs.loyalty.web.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuccessResponse extends AbstractResponse {

	private static final long serialVersionUID = 1L;
	
	private String resultUrl;
	
	public SuccessResponse(String message) {
		super(message);
	}
	
	public SuccessResponse(String message, String resultUrl) {
		super(message);
		this.resultUrl = resultUrl;
	}

}
