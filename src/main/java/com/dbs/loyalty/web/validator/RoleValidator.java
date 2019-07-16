package com.dbs.loyalty.web.validator;

import static com.dbs.loyalty.config.constant.ValidationConstant.VALIDATION_EXIST;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.service.RoleService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleValidator implements Validator {

	private final RoleService roleService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Role.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Role role = (Role) target;

		if (roleService.isNameExist(role.getId(), role.getName())) {
			Object[] errorArgs = new String[] { role.getName() };
			String defaultMessage = MessageUtil.getMessage(VALIDATION_EXIST, errorArgs);
			errors.rejectValue(DomainConstant.NAME, VALIDATION_EXIST, errorArgs, defaultMessage);
		}

	}

}
