package com.dbs.priviledge.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.priviledge.domain.User;
import com.dbs.priviledge.service.MessageService;
import com.dbs.priviledge.service.UserService;

public class UserValidator implements Validator {

	private final String EMAIL_EXIST = "validation.exist.email";
	
	private final String EMAIL = "email";

	private final UserService userService;

	public UserValidator(final UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		if (userService.isEmailExist(user)) {
			Object[] errorArgs = new String[] { user.getEmail() };
			String defaultMessage = MessageService.getMessage(EMAIL_EXIST, errorArgs);
			errors.rejectValue(EMAIL, EMAIL_EXIST, errorArgs, defaultMessage);
		}

	}

}
