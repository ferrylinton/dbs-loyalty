package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.repository.AuthorityRepository;

@Service
public class AuthorityService{
	
	private final AuthorityRepository authorityRepository;

	private AuthorityService(AuthorityRepository authorityRepository) {
		this.authorityRepository = authorityRepository;
	}
	
	public List<Authority> findAll(){
		return authorityRepository.findAll();
	}
	
	public Page<Authority> findAll(String keyword, Pageable pageable) {
		if(keyword == null || keyword.trim().equals(Constant.EMPTY)) {
			return authorityRepository.findAll(pageable);
		}else {
			return authorityRepository.findAllByNameContainingAllIgnoreCase(keyword, pageable);
		}
	}

	public Optional<Authority> findById(String id){
		return authorityRepository.findById(id);
	}
	
	public Optional<Authority> findByName(String name){
		return authorityRepository.findByNameIgnoreCase(name);
	}
}
