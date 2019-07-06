package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.HealthPackage;
import com.dbs.loyalty.repository.HealthPackageRepository;

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
	
}
