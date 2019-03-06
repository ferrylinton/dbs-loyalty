package com.dbs.loyalty.web.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.service.AuthorityService;

@Component
public class AuthorityConverter implements Converter<String, Authority> {
	
	private final AuthorityService authorityService;
	
	public AuthorityConverter(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}
	
	@Override
	public Authority convert(String id) {
		Optional<Authority> authority = authorityService.findById(id);
		
		if(authority.isPresent()) {
			return authority.get();
		}else {
			return null;
		}
	}

}
