package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.sec.Authority;

@Component
public class AuthorityConverter implements Converter<String, Authority> {
	
	@Override
	public Authority convert(String id) {
		Authority authority = new Authority();
		authority.setId(id);
		return authority;
	}

}
