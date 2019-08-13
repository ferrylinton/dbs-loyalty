package com.dbs.loyalty.web.validator;


import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.ValidationConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerValidator implements Validator {
	
	private static final String SECONDARY_CITY = "secondary.city";
	
	private final CustomerService customerService;
	
	private final ApplicationProperties applicationProperties;

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
		
		if(!StringUtils.isBlank(customer.getSecondary().getAddress()) && customer.getSecondary().getCity() == null) {
			errors.rejectValue(SECONDARY_CITY, ValidationConstant.NOT_NULL);
		}

		if(!customer.getMultipartFileImage().isEmpty()) {
			if(customer.getMultipartFileImage().getSize() > applicationProperties.getFile().getImageMaxSize()) {
				long maxSize = applicationProperties.getFile().getImageMaxSize() / (1024 * 1024);
				Object[] errorArgs = new Object[] { maxSize };
				String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_FILE_SIZE, errorArgs);
				errors.rejectValue(DomainConstant.MULTIPART_FILE_IMAGE, ValidationConstant.VALIDATION_FILE_SIZE, errorArgs, defaultMessage);
			}else if(!applicationProperties.getFile().getImageContentTypes().contains(customer.getMultipartFileImage().getContentType())) {
				String defaultMessage = MessageUtil.getMessage(ValidationConstant.VALIDATION_NOT_IMAGE);
				errors.rejectValue(DomainConstant.MULTIPART_FILE_IMAGE, ValidationConstant.VALIDATION_NOT_IMAGE, defaultMessage);
			}
		}
		
	}

}
