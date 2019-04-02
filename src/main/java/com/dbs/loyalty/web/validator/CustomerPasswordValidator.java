package com.dbs.loyalty.web.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerPasswordValidator implements Validator {

	private String CONFIRM_NEW_PASSWORD_NOT_MATCH = "validation.confirm.new.password";

	private String CONFIRM_NEW_PASSWORD = "confirmNewPassword";
	
	private String OLD_PASSWORD_NOT_MATCH = "validation.old.password";
	
	private String OLD_PASSWORD = "oldPassword";

	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerPasswordDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerPasswordDto customerPasswordDto = (CustomerPasswordDto) target;
		Optional<CustomerDto> customerDto = customerService.findByEmail(SecurityUtil.getLogged());
		
		if(customerPasswordDto.getOldPassword() != null && customerPasswordDto.getNewPassword() != null && customerPasswordDto.getConfirmNewPassword() != null) {
			if(!customerPasswordDto.getNewPassword().equals(customerPasswordDto.getConfirmNewPassword())) {
				Object[] errorArgs = new String[] { customerPasswordDto.getConfirmNewPassword() };
				String defaultMessage = MessageService.getMessage(CONFIRM_NEW_PASSWORD_NOT_MATCH, errorArgs);
				errors.rejectValue(CONFIRM_NEW_PASSWORD, CONFIRM_NEW_PASSWORD_NOT_MATCH, errorArgs, defaultMessage);
			}else if(!PasswordUtil.getInstance().matches(customerPasswordDto.getOldPassword(), customerDto.get().getPasswordHash())) {
				Object[] errorArgs = new String[] { customerPasswordDto.getOldPassword() };
				String defaultMessage = MessageService.getMessage(OLD_PASSWORD_NOT_MATCH, errorArgs);
				errors.rejectValue(OLD_PASSWORD, OLD_PASSWORD_NOT_MATCH, errorArgs, defaultMessage);
			}
		}
	}

}
