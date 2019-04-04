package com.dbs.loyalty.web.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public abstract class AbstractResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String message;

}
