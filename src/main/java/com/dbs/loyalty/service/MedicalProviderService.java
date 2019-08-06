package com.dbs.loyalty.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.MedicalProvider;
import com.dbs.loyalty.repository.MedicalProviderRepository;
import com.dbs.loyalty.service.specification.MedicalProviderSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MedicalProviderService {

	private Sort sortByName = Sort.by(DomainConstant.NAME);
	
	private final MedicalProviderRepository medicalProviderRepository;
	
	public Optional<MedicalProvider> findById(String id){
		return medicalProviderRepository.findById(id);
	}
	
	public List<MedicalProvider> findAll(){
		return medicalProviderRepository.findAll(sortByName);
	}
	
	public Page<MedicalProvider> findAll(Map<String, String> params, Pageable pageable) {
		return medicalProviderRepository.findAll(new MedicalProviderSpec(params), pageable);
	}
	
}
