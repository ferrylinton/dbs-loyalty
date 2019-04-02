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

	private String CONFIRM_NEW_PASS_NOT_MATCH = "validation.confirm.new.password";

	private String CONFIRM_NEW_PASS = "confirmNewPassword";
	
	private String OLD_PASS_NOT_MATCH = "validation.old.password";
	
	private String OLD_PASS = "oldPassword";

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
				String defaultMessage = MessageService.getMessage(CONFIRM_NEW_PASS_NOT_MATCH, errorArgs);
				errors.rejectValue(CONFIRM_NEW_PASS, CONFIRM_NEW_PASS_NOT_MATCH, errorArgs, defaultMessage);
			}else {
				Optional<CustomerDto> current = customerService.findByEmail(SecurityUtil.getLogged());
				if(current.isPresent() && !PasswordUtil.getInstance().matches(customerPasswordDto.getOldPassword(), current.get().getPasswordHash())) {
					Object[] errorArgs = new String[] { customerPasswordDto.getOldPassword() };
					String defaultMessage = MessageService.getMessage(OLD_PASS_NOT_MATCH, errorArgs);
					errors.rejectValue(OLD_PASS, OLD_PASS_NOT_MATCH, errorArgs, defaultMessage);
				}
			}
		}
	}

}
