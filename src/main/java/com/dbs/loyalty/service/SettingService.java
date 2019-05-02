package com.dbs.loyalty.service;

import java.time.Instant;
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
	
	public static final String DATETIME = "datetime";
	
	public static final String DATETIME_PATTERN = "DD/MM/YYYY HH:mm:ss";
	
	public static final String DATE = "date";
	
	public static final String DATE_PATTERN = "DD/MM/YYYY";
	
	public static final String TIME = "time";
	
	public static final String TIME_PATTERN = "HH:mm";
	
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
	
	public String datetime() {
		if(settings().containsKey(DATETIME)) {
			return settings().get(DATETIME);
		}else {
			return DATETIME_PATTERN;
		}
	}
	
	public String date() {
		if(settings().containsKey(DATE)) {
			return settings().get(DATE);
		}else {
			return DATE_PATTERN;
		}
	}
	
	public String time(Instant date) {
		if(settings().containsKey(TIME)) {
			return settings().get(TIME);
		}else {
			return TIME_PATTERN;
		}
	}

}
