package com.dbs.loyalty.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.LovedOne;
import com.dbs.loyalty.repository.LovedOneRepository;

@Service
public class LovedOneService {

	private final LovedOneRepository lovedOneRepository;
	
	public LovedOneService(LovedOneRepository lovedOneRepository) {
		this.lovedOneRepository = lovedOneRepository;
	}
	
	public Page<LovedOne> findAll(Pageable pageable){
		return lovedOneRepository.findAll(pageable);
	}
	
}
