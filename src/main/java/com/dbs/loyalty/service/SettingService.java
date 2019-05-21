package com.dbs.loyalty.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SettingService {

	public static final String JS_DATETIME = "DD-MM-YYYY HH:mm";
	
	public static final String JS_DATE = "DD-MM-YYYY";

	public static final String JS_TIME = "HH:mm";
	
	public static final String JAVA_DATETIME = "dd-MM-yyyy HH:mm";

	public static final String JAVA_DATE = "dd-MM-yyyy";

	public static final String JAVA_TIME = "HH:mm";

	
	public String jsDatetime() {
		return JS_DATETIME;
	}
	
	public String jsDate() {
		return JS_DATE;
	}
	
	public String jsTime() {
		return JS_TIME;
	}
	
	public String javaDatetime() {
		return JAVA_DATETIME;
	}
	
	public String javaDate() {
		return JAVA_DATE;
	}
	
	public String javaTime() {
		return JAVA_TIME;
	}

}
