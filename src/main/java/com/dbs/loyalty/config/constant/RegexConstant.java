package com.dbs.loyalty.config.constant;

public final class RegexConstant {
	
	public static final String ALPHABET 			= "^[a-zA-Z]*$";
	
	public static final String ALPHABET_MESSAGE		= "{validation.pattern.alphabet}";
	
	public static final String USERNAME 			= "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+$";
	
	public static final String USERNAME_MESSAGE		= "{validation.pattern.username}";
	
	public static final String PASSWORD				= "^[^\\s\\\\]+$";
	
	public static final String PASSWORD_MESSAGE		= "validation.pattern.password";

	public static final String EMAIL 				= "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

	public static final String EMAIL_MESSAGE		= "{validation.pattern.email}";
	
	public static final String NAME 				= "^[a-zA-Z0-9 ]*$";
	
	public static final String NAME_MESSAGE			= "{validation.pattern.name}";
	
	public static final String ALPHANUMERIC			= "^[a-zA-Z0-9]*$";
	
	public static final String ALPHANUMERIC_MESSAGE	= "{validation.pattern.alphanumeric}";
	
	public static final String UTC					= "^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(.[0-9]+)?(Z)$";
	
	public static final String UTC_MESSAGE			= "{validation.pattern.utc}";
	
	public static final String DATETIME				= "^([0][1-9]|[1][0-9]|[2][0-9]|[3][0-1])-([0][1-9]|[1][0-2])-([1][9][0-9]{2}|[2][0-9]{3})( ([0-1][0-9]|[2][0-3]):[0-5][0-9])$";
	
	public static final String DATETIME_MESSAGE		= "{validation.pattern.datetime}";
	
	public static final String DATE					= "^([0][1-9]|[1][0-9]|[2][0-9]|[3][0-1])-([0][1-9]|[1][0-2])-([1][9][0-9]{2}|[2][0-9]{3})( ([0-1][0-9]|[2][0-3]):[0-5][0-9])$";
	
	public static final String DATE_MESSAGE			= "{validation.pattern.date}";
	
	private RegexConstant() {
		// hide constructor
	}
	
}