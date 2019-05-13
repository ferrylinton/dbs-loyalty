package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.Authority;

@Component
public class AuthorityConverter implements Converter<String, Authority> {
	
	@Override
	public Authority convert(String content) {
		if(content  != null && content.length() > 22) {
			Authority authority = new Authority();
			authority.setId(content.substring(0,22));
			authority.setName(content.replace(authority.getId(), Constant.EMPTY));
			return authority;
		}else {
			return null;
		}

	}

}
