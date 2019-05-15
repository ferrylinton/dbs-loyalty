package com.dbs.loyalty.web.validator;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.RegexConstant;
import com.dbs.loyalty.config.constant.UserTypeConstant;
import com.dbs.loyalty.config.constant.ValidationConstant;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.service.UserLdapService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserValidator implements Validator {

	private final UserService userService;
	
	private final UserLdapService userLdapService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		if(user.getId() == null && (user.getUserType().equals(UserTypeConstant.EXTERNAL))){
			if((user.getPasswordPlain() == null || user.getPasswordPlain().trim().length() < 6 || user.getPasswordPlain().trim().length() > 30)) {
				Object[] errorArgs = new Object[] {user.getPasswordPlain(), 6, 30 };
				String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_SIZE_PASSWORD, errorArgs);
				errors.rejectValue(DomainConstant.PASSWORD_PLAIN, ValidationConstant.VALIDATION_SIZE_PASSWORD, errorArgs, defaultMessage);
			}else {
				Pattern pattern = Pattern.compile(RegexConstant.PASSWORD);
				
				if(!pattern.matcher(user.getPasswordPlain()).matches()) {
					String defaultMessage = MessageUtil.getMessage(RegexConstant.PASSWORD_MESSAGE);
					errors.rejectValue(DomainConstant.PASSWORD_PLAIN, RegexConstant.PASSWORD_MESSAGE, defaultMessage);
				}
			}
		}

		if (userService.isUsernameExist(user.getId(), user.getUsername())) {
			Object[] errorArgs = new String[] { user.getUsername() };
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EXIST, errorArgs);
			errors.rejectValue(DomainConstant.USERNAME, ValidationConstant.VALIDATION_EXIST, errorArgs, defaultMessage);
		}
		
		if(user.getUserType().equals(UserTypeConstant.INTERNAL) && !userLdapService.isUserExist(user.getUsername())) {
			Object[] errorArgs = new String[] { user.getUsername() };
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_USER_LDAP_NOT_FOUND, errorArgs);
			errors.rejectValue(DomainConstant.USERNAME, ValidationConstant.VALIDATION_USER_LDAP_NOT_FOUND, errorArgs, defaultMessage);
		}

	}

}
