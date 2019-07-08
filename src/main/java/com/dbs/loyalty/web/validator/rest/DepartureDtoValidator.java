package com.dbs.loyalty.web.validator.rest;

import java.time.Instant;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.config.constant.RegexConstant;
import com.dbs.loyalty.service.DepartureService;
import com.dbs.loyalty.service.dto.DepartureDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DepartureDtoValidator implements Validator {
	
	private final DepartureService departureService;

	@Override
	public boolean supports(Class<?> clazz) {
		return DepartureDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		DepartureDto departureDto = (DepartureDto) target;
		Pattern utcPattern = Pattern.compile(RegexConstant.UTC);
		String message;

		if(!utcPattern.matcher(departureDto.getFlightDate()).matches()) {
			message = String.format("Invalid utc date format [format=%s]", DateConstant.UTC);
			errors.rejectValue("flightDate", message, message);
		}else {
			try {
				Instant date = Instant.parse(departureDto.getFlightDate());
	            
				if(!date.isAfter(Instant.now())) {
					message = "Invalid date, must equal or after today";
					errors.rejectValue("flightDate", message, message);
				}else if(departureService.isExist(departureDto.getAirport().getId(), date)) {
					message = "Data is already exist";
					errors.rejectValue("message", message, message);
				}
				
	        } catch (Exception e) {
	        	log.error(e.getLocalizedMessage(), e);
	        	errors.rejectValue("message", e.getLocalizedMessage(), e.getLocalizedMessage());
	        }
		}
		
		if(departureDto.getPickupTime() != null && !utcPattern.matcher(departureDto.getPickupTime()).matches()) {
			message = String.format("Invalid utc date format [format=%s]", DateConstant.UTC);
			errors.rejectValue("pickupTime", message, message);
		}
		
	}

}
