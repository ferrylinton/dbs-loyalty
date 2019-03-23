package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.service.dto.AuthorityDto;

@Component
public class AuthorityConverter implements Converter<String, AuthorityDto> {
	
	@Override
	public AuthorityDto convert(String content) {
		String[] str = content.split(Constant.COMMA);

		if(str.length == 2) {
			return new AuthorityDto(str[0], str[1]);
		}else {
			return null;
		}

	}

}
