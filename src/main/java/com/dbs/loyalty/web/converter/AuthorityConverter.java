package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.repository.AuthorityRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthorityConverter implements Converter<String, Authority> {
	
	private final AuthorityRepository authorityRepository;
	
	@Override
	public Authority convert(String id) {
		return authorityRepository.getOne(id);
	}

}
