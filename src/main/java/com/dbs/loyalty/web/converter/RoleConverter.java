package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.Role;

@Component
public class RoleConverter implements Converter<String, Role> {

	@Override
	public Role convert(String content) {
		if(content  != null && content.length() > 22) {
			Role role = new Role();
			role.setId(content.substring(0,22));
			role.setName(content.replace(role.getId(), Constant.EMPTY));
			return role;
		}else {
			return null;
		}
	}

}
