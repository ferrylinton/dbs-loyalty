package com.dbs.loyalty.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.sec.Authority;
import com.dbs.loyalty.repository.AuthorityRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthorityService{

	public static final Sort SORT_BY = Sort.by("name");
	
	private final AuthorityRepository authorityRepository;

	public List<Authority> findAll(){
		return authorityRepository.findAll(SORT_BY);
	}

}
