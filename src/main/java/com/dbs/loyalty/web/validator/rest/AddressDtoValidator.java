package com.dbs.loyalty.web.validator.rest;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.AddressLabelConstant;
import com.dbs.loyalty.domain.City;
import com.dbs.loyalty.service.CityService;
import com.dbs.loyalty.service.dto.AddressDto;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class AddressDtoValidator implements Validator {
	
	private final CityService cityService;

	@Override
	public boolean supports(Class<?> clazz) {
		return AddressDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AddressDto addressDto = (AddressDto) target;
		
		if(addressDto.getLabel() != null && !(AddressLabelConstant.PRIMARY.equalsIgnoreCase(addressDto.getLabel()) || 
				AddressLabelConstant.SECONDARY.equalsIgnoreCase(addressDto.getLabel()))) {
			
			String message = String.format("label must be '%s' or '%s'", AddressLabelConstant.PRIMARY, AddressLabelConstant.SECONDARY);
			errors.rejectValue("label", message, message);
		}
		
		Optional<City> current = cityService.findByNameIgnoreCase(addressDto.getCity());
		
		if(!current.isPresent()) {
			String message = String.format("city [%s] is not valid", addressDto.getCity());
			errors.rejectValue("city", message, message);
		}else {
			addressDto.setCity(current.get().getName());
		}
		
	}

}
