package com.dbs.loyalty.config.constant;

public final class ValidationConstant {
	
	public static final String VALIDATION_EXIST = "validation.exist";
	
	public static final String VALIDATION_SIZE_PASSWORD = "validation.size.password";
	
	public static final String VALIDATION_USER_LDAP_NOT_FOUND = "validation.userLdapNotFound";
	
	public static final String VALIDATION_EMPTY_FILE = "validation.empty.file";
	
	public static final String VALIDATION_NOT_IMAGE = "validation.not.image";
	
	public static final String VALIDATION_NOT_PDF = "validation.not.pdf";
	
	public static final String VALIDATION_FILE_SIZE = "validation.file.size";
	
	public static final String VALIDATION_SIZE = "javax.validation.constraints.Size.message";
	
	public static final String NOT_NULL = "javax.validation.constraints.NotNull.message";
	
	public static final String NOT_EMPTY = "javax.validation.constraints.NotEmpty.message";

	private ValidationConstant() {
		// hide constructor
	}
}
