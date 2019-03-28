package com.dbs.loyalty.service.specification;

import java.time.format.DateTimeFormatter;

public class Constant {
	
	public static final DateTimeFormatter FORMATTER =  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	
	public static final String START_DATE_FORMAT = "%s 00:00";
	
	public static final String END_DATE_FORMAT = "%s 11:59";
	
	public static final String EMPTY = "";

	public static final String LIKE_FORMAT = "%%%s%%";

	public static final String KY_PARAM = "ky";
	
	public static final String START_DATE_PARAM = "sd";
	
	public static final String END_DATE_PARAM = "ed";
	
	public static final String ID = "id";
	
	public static final String CODE = "code";
	
	public static final String DESCRIPTION = "description";
	
	public static final String USERNAME = "username";
	
	public static final String EMAIL = "email";
	
	public static final String NAME = "name";
	
	public static final String LOGIN_STATUS = "loginStatus";
	
	public static final String IP = "ip";
	
	public static final String TITLE = "title";
	
	public static final String CREATED_DATE = "createdDate";
	
	public static final String BROWSER = "browser";
	
	public static final String DEVICE_TYPE = "deviceType";
	
}
