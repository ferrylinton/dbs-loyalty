package com.dbs.loyalty.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.PriviledgeProduct;
import com.dbs.loyalty.repository.PriviledgeProductRepository;
import com.dbs.loyalty.service.specification.PriviledgeProductSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PriviledgeProductService{

	private final PriviledgeProductRepository priviledgeProductRepository;

	public Optional<PriviledgeProduct> findById(String id){
		return priviledgeProductRepository.findById(id);
	}
	
	public Optional<String> findTermAndConditionById(String id){
		return priviledgeProductRepository.findTermAndConditionById(id);
	}
	
	public List<PriviledgeProduct> findAll(){
		return priviledgeProductRepository.findAll();
	}
	
	public Page<PriviledgeProduct> findAll(Map<String, String> params, Pageable pageable) {
		return priviledgeProductRepository.findAll(new PriviledgeProductSpec(params), pageable);
	}
	
}
