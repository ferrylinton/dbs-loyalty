package com.dbs.loyalty.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerValidator implements Validator {
	
	private String validationEmptyMultipartFileImage = "validation.empty.multipartFileImage";

	private String multipartFileImage = "multipartFileImage";

	private String validationExistEmail = "validation.exist.email";

	private String email = "email";
	
	private String passSize = "validation.size.password";
	
	private String passPlain = "passwordPlain";
	
	private final CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Customer.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Customer customer = (Customer) target;

		if(customer.getId() == null && (customer.getPasswordPlain() == null || customer.getPasswordPlain().trim().length() < 6 || customer.getPasswordPlain().trim().length() > 30)) {
			Object[] errorArgs = new Object[] {customer.getPasswordPlain(), 6, 30 };
			String defaultMessage = MessageUtil.getMessage(passSize, errorArgs);
			errors.rejectValue(passPlain, passSize, errorArgs, defaultMessage);
		}
		
		if (customerService.isEmailExist(customer.getId(), customer.getEmail())) {
			Object[] errorArgs = new String[] { customer.getEmail() };
			String defaultMessage = MessageUtil.getMessage(validationExistEmail, errorArgs);
			errors.rejectValue(email, validationExistEmail, errorArgs, defaultMessage);
		}
		
		if(customer.getId() == null && customer.getMultipartFileImage().isEmpty()) {
			String defaultMessage = MessageUtil.getMessage(validationEmptyMultipartFileImage);
			errors.rejectValue(multipartFileImage, validationEmptyMultipartFileImage, defaultMessage);
		}

	}

}
