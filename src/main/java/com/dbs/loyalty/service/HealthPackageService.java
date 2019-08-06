package com.dbs.loyalty.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.HealthPackage;
import com.dbs.loyalty.repository.HealthPackageRepository;
import com.dbs.loyalty.service.specification.HealthPackageSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HealthPackageService {

	private static final Sort SORT_BY = Sort.by("name");
	
	private final HealthPackageRepository healthPackageRepository;
	
	public List<HealthPackage> findAll(){
		return healthPackageRepository.findAll(SORT_BY);
	}
	
	public Optional<HealthPackage> findById(String id){
		return healthPackageRepository.findById(id);
	}
	
	public Page<HealthPackage> findAll(Map<String, String> params, Pageable pageable) {
		return healthPackageRepository.findAll(new HealthPackageSpec(params), pageable);
	}
	
}
