package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.enumeration.UserType;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.dto.UserDto;

public class UserValidator implements Validator {
	
	private final String PASSWORD_SIZE = "validation.size.password";
	
	private final String PASSWORD = "passwordPlain";

	@Override
	public boolean supports(Class<?> clazz) {
		return UserDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserDto userDto = (UserDto) target;
		
		if(userDto.getId() == null && (userDto.getUserType() == UserType.External) && (userDto.getPasswordPlain() == null || userDto.getPasswordPlain().trim().length() < 6 || userDto.getPasswordPlain().trim().length() > 30)) {
			String defaultMessage = MessageService.getMessage(PASSWORD_SIZE);
			errors.rejectValue(PASSWORD, PASSWORD_SIZE, defaultMessage);
		}

	}

}
