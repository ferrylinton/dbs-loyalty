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
	
}
