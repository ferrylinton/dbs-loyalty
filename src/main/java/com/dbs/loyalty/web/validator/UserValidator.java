package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.UserService;

public class UserValidator implements Validator {

	private final String EMAIL_EXIST = "validation.exist.email";
	
	private final String EMAIL = "email";
	
	private final String PASSWORD_SIZE = "validation.size.password";
	
	private final String PASSWORD = "passwordPlain";

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
		
		if(user.getId() == null && user.getAuthenticateFromDb() && (user.getPasswordPlain() == null || user.getPasswordPlain().trim().length() < 6 || user.getPasswordPlain().trim().length() > 30)) {
			String defaultMessage = MessageService.getMessage(PASSWORD_SIZE);
			errors.rejectValue(PASSWORD, PASSWORD_SIZE, defaultMessage);
		}else if (userService.isEmailExist(user)) {
			Object[] errorArgs = new String[] { user.getEmail() };
			String defaultMessage = MessageService.getMessage(EMAIL_EXIST, errorArgs);
			errors.rejectValue(EMAIL, EMAIL_EXIST, errorArgs, defaultMessage);
		}

	}

}
