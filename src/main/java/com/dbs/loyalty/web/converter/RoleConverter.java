package com.dbs.loyalty.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RoleConverter implements Converter<String, Role> {

	private final RoleRepository roleRepository;
	
	@Override
	public Role convert(String id) {
		if(id != null) {
			return roleRepository.getOne(id);
		}else {
			return null;
		}
	}

}
