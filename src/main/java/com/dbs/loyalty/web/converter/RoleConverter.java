package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.service.dto.RoleDto;

@Component
public class RoleConverter implements Converter<String, RoleDto> {

	@Override
	public RoleDto convert(String content) {
		String[] str = content.split(Constant.COMMA);
		
		if(str.length == 2) {
			return new RoleDto(str[0], str[1]);
		}else {
			return null;
		}
	}

}
