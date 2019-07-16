package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.PriviledgeProduct;
import com.dbs.loyalty.repository.PriviledgeProductRepository;

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
	
}
