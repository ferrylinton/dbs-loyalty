package com.dbs.loyalty.web.validator.rest;

import java.time.Instant;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.config.constant.RegexConstant;
import com.dbs.loyalty.service.ArrivalService;
import com.dbs.loyalty.service.dto.ArrivalDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ArrivalDtoValidator implements Validator {
	
	private final ArrivalService arrivalService;

	@Override
	public boolean supports(Class<?> clazz) {
		return ArrivalDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ArrivalDto arrivalDto = (ArrivalDto) target;
		Pattern utcPattern = Pattern.compile(RegexConstant.UTC);
		String message;

		if(!utcPattern.matcher(arrivalDto.getFlightDate()).matches()) {
			message = String.format("Invalid utc date format [format=%s]", DateConstant.UTC);
			errors.rejectValue("flightDate", message, message);
		}else {
			try {
				Instant date = Instant.parse(arrivalDto.getFlightDate());
	            
				if(!date.isAfter(Instant.now())) {
					message = "Invalid date, must equal or after today";
					errors.rejectValue("flightDate", message, message);
				}else if(arrivalService.isExist(arrivalDto.getAirport().getId(), date)) {
					message = "Data is already exist";
					errors.rejectValue("message", message, message);
				}
				
	        } catch (Exception e) {
	        	log.error(e.getLocalizedMessage(), e);
	        	errors.rejectValue("message", e.getLocalizedMessage(), e.getLocalizedMessage());
	        }
		}
		
		if(arrivalDto.getPickupTime() != null && !utcPattern.matcher(arrivalDto.getPickupTime()).matches()) {
			message = String.format("Invalid utc date format [format=%s]", DateConstant.UTC);
			errors.rejectValue("pickupTime", message, message);
		}
		
	}

}
