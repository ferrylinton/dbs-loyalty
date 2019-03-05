package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.RoleService;

public class RoleValidator implements Validator {

	private final String NAME_EXIST = "validation.exist.name";

	private final String NAME = "name";

	private final RoleService roleService;

	public RoleValidator(final RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Role.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Role role = (Role) target;

		if (roleService.isNameExist(role)) {
			Object[] errorArgs = new String[] { role.getName() };
			String defaultMessage = MessageService.getMessage(NAME_EXIST, errorArgs);
			errors.rejectValue(NAME, NAME_EXIST, errorArgs, defaultMessage);
		}

	}

}
