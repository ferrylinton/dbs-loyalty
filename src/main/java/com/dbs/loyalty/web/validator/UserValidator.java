package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.domain.enumeration.UserType;
import com.dbs.loyalty.service.UserLdapService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserValidator implements Validator {
	
	private String validationUserLdapNotFound = "validation.userLdapNotFound";
	
	private String validationExistUsername = "validation.exist.username";

	private String username = "username";
	
	private String passSize = "validation.size.password";
	
	private String passPlain = "passwordPlain";

	private final UserService userService;
	
	private final UserLdapService userLdapService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		if(user.getId() == null && (user.getUserType() == UserType.EXTERNAL) && (user.getPasswordPlain() == null || user.getPasswordPlain().trim().length() < 6 || user.getPasswordPlain().trim().length() > 30)) {
			String defaultMessage = MessageUtil.getMessage(passSize);
			errors.rejectValue(passPlain, passSize, defaultMessage);
		}
		
		if (userService.isUsernameExist(user.getId(), user.getUsername())) {
			Object[] errorArgs = new String[] { user.getUsername() };
			String defaultMessage = MessageUtil.getMessage(validationExistUsername, errorArgs);
			errors.rejectValue(username, validationExistUsername, errorArgs, defaultMessage);
		}
		
		if(user.getUserType() == UserType.INTERNAL && !userLdapService.isUserExist(user.getUsername())) {
			Object[] errorArgs = new String[] { user.getUsername() };
			String defaultMessage = MessageUtil.getMessage(validationUserLdapNotFound, errorArgs);
			errors.rejectValue(username, validationUserLdapNotFound, errorArgs, defaultMessage);
		}

	}

}
