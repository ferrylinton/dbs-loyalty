package com.dbs.loyalty.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.CachingConstant;
import com.dbs.loyalty.domain.Setting;
import com.dbs.loyalty.repository.SettingRepository;
import com.dbs.loyalty.service.specification.SettingSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SettingService {

	private final SettingRepository settingRepository;

	public Optional<Setting> findById(String id){
		return settingRepository.findById(id);
	}
	
	public Optional<Setting> findByName(String name) {
		return settingRepository.findByNameIgnoreCase(name);
	}
	
	@Cacheable(CachingConstant.SETTINGS)
	public Map<String, String> settings() {
		Map<String, String> map = new HashMap<String, String>();

		List<Setting> settings = settingRepository.findAll();

		for (Setting setting : settings) {
			map.put(setting.getName(), setting.getValue());
		}

		return map;
	}

	public Page<Setting> findAll(Map<String, String> params, Pageable pageable) {
		return settingRepository.findAll(new SettingSpec(params), pageable);
	}

	public boolean isNameExist(String id, String name) {
		Optional<Setting> setting = settingRepository.findByNameIgnoreCase(name);

		if (setting.isPresent()) {
			return (id == null) || (!id.equals(setting.get().getId()));
		}else {
			return false;
		}
	}

	public Setting save(Setting setting) {
		return settingRepository.save(setting);
	}
	
}