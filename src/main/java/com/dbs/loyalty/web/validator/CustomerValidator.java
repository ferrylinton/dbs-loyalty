package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.ValidationConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerValidator implements Validator {
	
	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Customer.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Customer customer = (Customer) target;
		
		if (customerService.isEmailExist(customer.getId(), customer.getEmail())) {
			Object[] errorArgs = new String[] { customer.getEmail() };
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EXIST, errorArgs);
			errors.rejectValue(DomainConstant.EMAIL, ValidationConstant.VALIDATION_EXIST, errorArgs, defaultMessage);
		}
		
		if(customer.getId() == null && customer.getMultipartFileImage().isEmpty()) {
			String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_EMPTY_FILE);
			errors.rejectValue(DomainConstant.MULTIPART_FILE_IMAGE, ValidationConstant.VALIDATION_EMPTY_FILE, defaultMessage);
		}

	}

}
