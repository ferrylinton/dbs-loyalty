package com.dbs.loyalty.config.constant;

public final class Constant {

	public static final String EMAIL_REGEX 			= "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
	
	public static final String USERNAME_REGEX 		= "^[A-Za-z][A-Za-z0-9_]{4,49}$";
	
	public static final String NAME_REGEX 			= "^[a-zA-Z0-9\\d\\-_\\s]*$";
	
	public static final String EMPTY 				= "";
	
	public static final String COMMA 				= ",";
	
	public static final String DOT 					= ".";
	
	public static final String ZERO					= "0";

	public static final String ERROR				= "error";
	
	public static final String PAGE 				= "page";
	
	public static final String SORT 				= "sort";
	
	public static final String KEYWORD 				= "keyword";
	
	public static final String MESSAGE				= "message";
	
	public static final String REQUEST_URI 			= "requestURI";

	public static final String ENTITY				= "entity";
	
	public static final String ENTITY_URL 			= "entityUrl";

	public static final String BR 					= "<br>";
	
	public static final int	SIZE					= 10;
	
	public static final String PARAMS				= "params";
	
	public static final String AND					= "&";
	
	public static final String EQ					= "=";
	
	public static final String NEXT					= "next";
	
	public static final String PREVIOUS				= "previous";
	
	public static final String ORDER				= "order";
	
	public static final String DELETE				= "delete";
	
	public static final String DEFAULT				= "default";
	
	public static final String TOTAL				= "total";
	
	public static final String UNAUTHORIZED 		= "Unauthorized";
	
	public static final String FORBIDDEN 			= "Forbidden";
	
	private Constant() {
		// hide constructor
	}
	
}