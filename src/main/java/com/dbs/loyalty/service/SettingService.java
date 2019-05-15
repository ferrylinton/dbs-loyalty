package com.dbs.loyalty.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.CachingConstant;
import com.dbs.loyalty.domain.Setting;
import com.dbs.loyalty.repository.SettingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SettingService {
	
	public static final String JS_DATETIME = "js-datetime";
	
	public static final String JS_DATETIME_PATTERN = "DD/MM/YYYY HH:mm:ss";
	
	public static final String JS_DATE = "js-date";
	
	public static final String JS_DATE_PATTERN = "DD/MM/YYYY";
	
	public static final String JS_TIME = "js-time";
	
	public static final String JS_TIME_PATTERN = "HH:mm";
	
	public static final String JAVA_DATETIME = "java-datetime";
	
	public static final String JAVA_DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
	
	public static final String JAVA_DATE = "java-date";
	
	public static final String JAVA_DATE_PATTERN = "dd/MM/yyyy";
	
	public static final String JAVA_TIME = "java-time";
	
	public static final String JAVA_TIME_PATTERN = "HH:mm";
	
	private final SettingRepository settingRepository;
	
	@Cacheable(CachingConstant.SETTINGS)
	public Map<String, String> settings() {
		Map<String, String> map = new HashMap<String, String>();
		
		List<Setting> settings = settingRepository.findAll();
		
		for(Setting setting : settings) {
			map.put(setting.getName(), setting.getValue());
		}
		
		return map;
	}
	
	public String jsDatetime() {
		if(settings().containsKey(JS_DATETIME)) {
			return settings().get(JS_DATETIME);
		}else {
			return JS_DATETIME_PATTERN;
		}
	}
	
	public String jsDate() {
		if(settings().containsKey(JS_DATE)) {
			return settings().get(JS_DATE);
		}else {
			return JS_DATE_PATTERN;
		}
	}
	
	public String jsTime() {
		if(settings().containsKey(JS_TIME)) {
			return settings().get(JS_TIME);
		}else {
			return JS_TIME_PATTERN;
		}
	}
	
	public String javaDatetime() {
		if(settings().containsKey(JAVA_DATETIME)) {
			return settings().get(JAVA_DATETIME);
		}else {
			return JAVA_DATETIME_PATTERN;
		}
	}
	
	public String javaDate() {
		if(settings().containsKey(JAVA_DATE)) {
			return settings().get(JAVA_DATE);
		}else {
			return JAVA_DATE_PATTERN;
		}
	}
	
	public String javaTime() {
		if(settings().containsKey(JAVA_TIME)) {
			return settings().get(JAVA_TIME);
		}else {
			return JAVA_TIME_PATTERN;
		}
	}

}
