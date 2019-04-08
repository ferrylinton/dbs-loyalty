package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerValidator implements Validator {

	private String validationExistEmail = "validation.exist.email";
	
	private String validationNotEmptyFile = "validation.notempty.file";

	private String email = "email";
	
	private String file = "file";

	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CustomerDto customerDto = (CustomerDto) target;

		if (customerService.isEmailExist(customerDto)) {
			Object[] errorArgs = new String[] { customerDto.getEmail() };
			String defaultMessage = MessageUtil.getMessage(validationExistEmail, errorArgs);
			errors.rejectValue(email, validationExistEmail, errorArgs, defaultMessage);
		}
		
		if(customerDto.getId() == null && customerDto.getFile().isEmpty()) {
			String defaultMessage = MessageUtil.getMessage(validationNotEmptyFile);
			errors.rejectValue(file, validationNotEmptyFile, defaultMessage);
		}

	}

}
