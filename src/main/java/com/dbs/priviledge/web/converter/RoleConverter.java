package com.dbs.priviledge.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.priviledge.domain.Role;

@Component
public class RoleConverter implements Converter<String, Role> {

	@Override
	public Role convert(String id) {
		Role role = new Role();
		role.setId(id);
		return role;
	}

}
