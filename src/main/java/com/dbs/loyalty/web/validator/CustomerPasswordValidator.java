package com.dbs.loyalty.web.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.cst.Customer;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerPasswordValidator implements Validator {

	private String validationConfirmNewPass = "validation.confirm.new.password";

	private String confirmNewPass = "confirmNewPassword";
	
	private String oldPassNotMatch = "validation.old.password";
	
	private String oldPass = "oldPassword";

	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerPasswordDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerPasswordDto customerPasswordDto = (CustomerPasswordDto) target;
		
		
		if(customerPasswordDto.getOldPassword() != null && customerPasswordDto.getNewPassword() != null && customerPasswordDto.getConfirmNewPassword() != null) {
			if(!customerPasswordDto.getNewPassword().equals(customerPasswordDto.getConfirmNewPassword())) {
				Object[] errorArgs = new String[] { customerPasswordDto.getConfirmNewPassword() };
				String defaultMessage = MessageUtil.getMessage(validationConfirmNewPass, errorArgs);
				errors.rejectValue(confirmNewPass, validationConfirmNewPass, errorArgs, defaultMessage);
			}else {
				Optional<Customer> current = customerService.findByEmail(SecurityUtil.getLogged());
				
				if(current.isPresent() && !PasswordUtil.matches(customerPasswordDto.getOldPassword(), current.get().getPasswordHash())) {
					Object[] errorArgs = new String[] { customerPasswordDto.getOldPassword() };
					String defaultMessage = MessageUtil.getMessage(oldPassNotMatch, errorArgs);
					errors.rejectValue(oldPass, oldPassNotMatch, errorArgs, defaultMessage);
				}
			}
		}
	}

}
