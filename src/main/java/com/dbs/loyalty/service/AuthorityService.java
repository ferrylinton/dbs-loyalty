package com.dbs.loyalty.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.repository.AuthorityRepository;
import com.dbs.loyalty.service.dto.AuthorityDto;
import com.dbs.loyalty.service.mapper.AuthorityMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthorityService{
	
	private Sort sortByName = Sort.by("name");
	
	private final AuthorityRepository authorityRepository;
	
	private final AuthorityMapper authorityMapper;

	public List<AuthorityDto> findAll(){
		return authorityRepository.findAll(sortByName)
				.stream()
				.map(authorityMapper::toDto)
				.collect(Collectors.toList());
	}

}
