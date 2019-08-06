package com.dbs.loyalty.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.MedicalCity;
import com.dbs.loyalty.repository.MedicalCityRepository;
import com.dbs.loyalty.service.specification.MedicalCitySpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MedicalCityService {

	private Sort sortBy = Sort.by(DomainConstant.NAME);
	
	private final MedicalCityRepository medicalCityRepository;
	
	public Optional<MedicalCity> findById(String id){
		return medicalCityRepository.findById(id);
	}
	
	public List<MedicalCity> findByMedicalProviderId(String medicalProviderId){
		return medicalCityRepository.findByMedicalProviderId(medicalProviderId, sortBy);
	}
	
	public Page<MedicalCity> findAll(Map<String, String> params, Pageable pageable) {
		return medicalCityRepository.findAll(new MedicalCitySpec(params), pageable);
	}
	
}
