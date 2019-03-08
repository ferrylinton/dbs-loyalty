package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Authority;

@Component
public class AuthorityConverter implements Converter<String, Authority> {
	
	@Override
	public Authority convert(String content) {
		String[] str = content.split(Constant.COMMA);
		
		Authority authority = new Authority();
		
		if(str.length == 2) {
			authority.setId(str[0]);
			authority.setName(str[1]);
		}
		
		return authority;
	}

}
