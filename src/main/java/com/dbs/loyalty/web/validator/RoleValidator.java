package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.service.dto.RoleDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleValidator implements Validator {

	private String NAME_EXIST = "validation.exist.name";

	private String NAME = "name";

	private final RoleService roleService;

	@Override
	public boolean supports(Class<?> clazz) {
		return RoleDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		RoleDto roleDto = (RoleDto) target;

		if (roleService.isNameExist(roleDto)) {
			Object[] errorArgs = new String[] { roleDto.getName() };
			String defaultMessage = MessageService.getMessage(NAME_EXIST, errorArgs);
			errors.rejectValue(NAME, NAME_EXIST, errorArgs, defaultMessage);
		}

	}

}
