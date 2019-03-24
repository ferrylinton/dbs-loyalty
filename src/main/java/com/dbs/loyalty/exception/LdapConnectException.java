package com.dbs.loyalty.exception;

import org.springframework.security.core.AuthenticationException;

public class LdapConnectException extends AuthenticationException{

	private static final long serialVersionUID = 1L;

	public LdapConnectException(String msg) {
		super(msg);
	}
	
	public LdapConnectException(String msg, Throwable t) {
		super(msg, t);
	}
	
}
