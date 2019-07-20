package com.dbs.loyalty.service;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
	
	private static final String DEFAULT_CURRENCY = "IDR";
	
	private static final Integer DEFAULT_POINT_TO_RUPIAH = 1000;

	private final SettingRepository settingRepository;

	public Optional<Setting> findById(String id){
		return settingRepository.findById(id);
	}
	
	public Optional<Setting> findByName(String name) {
		return settingRepository.findByNameIgnoreCase(name);
	}
	
	@Cacheable(CachingConstant.POINT_TO_RUPIAH)
	public Integer getPointToRupiah() {
		Optional<Setting> setting = settingRepository.findByNameIgnoreCase(CachingConstant.POINT_TO_RUPIAH);

		if(setting.isPresent() && StringUtils.isNumeric(setting.get().getValue())) {
			return Integer.valueOf(setting.get().getValue());
		}

		return DEFAULT_POINT_TO_RUPIAH;
	}
	
	@Cacheable(CachingConstant.CURRENCY)
	public String getCurrency() {
		Optional<Setting> setting = settingRepository.findByNameIgnoreCase(CachingConstant.CURRENCY);

		if(setting.isPresent()) {
			return setting.get().getValue();
		}

		return DEFAULT_CURRENCY;
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