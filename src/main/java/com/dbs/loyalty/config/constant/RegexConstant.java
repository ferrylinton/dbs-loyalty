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
	
	private RegexConstant() {
		// hide constructor
	}
	
}