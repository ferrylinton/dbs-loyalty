package com.dbs.loyalty.service;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.DateConstant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SettingService {

	public String jsDatetime() {
		return DateConstant.JS_DATETIME;
	}
	
	public String jsDate() {
		return DateConstant.JS_DATE;
	}
	
	public String jsTime() {
		return DateConstant.JS_TIME;
	}
	
	public String javaDatetime() {
		return DateConstant.JAVA_DATETIME;
	}
	
	public String javaDate() {
		return DateConstant.JAVA_DATE;
	}
	
	public String javaTime() {
		return DateConstant.JAVA_TIME;
	}

}
