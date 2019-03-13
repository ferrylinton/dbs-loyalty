package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Role;

@Component
public class RoleConverter implements Converter<String, Role> {

	@Override
	public Role convert(String content) {
		String[] str = content.split(Constant.COMMA);
		Role role = new Role();
		
		if(str.length == 2) {
			role.setId(str[0]);
			role.setName(str[1]);
		}
		
		return role;
	}

}
