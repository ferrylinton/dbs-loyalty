package com.dbs.loyalty.web.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuccessResponse extends AbstractResponse {

	private String resultUrl;
	
	public SuccessResponse(String message, String resultUrl) {
		super(message);
		this.resultUrl = resultUrl;
	}

}
