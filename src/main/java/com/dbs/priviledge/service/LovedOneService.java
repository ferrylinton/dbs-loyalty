package com.dbs.priviledge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.priviledge.domain.LovedOne;
import com.dbs.priviledge.repository.LovedOneRepository;

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
