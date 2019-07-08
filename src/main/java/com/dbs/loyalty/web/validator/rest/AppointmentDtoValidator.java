package com.dbs.loyalty.web.validator.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.config.constant.RegexConstant;
import com.dbs.loyalty.service.AppointmentService;
import com.dbs.loyalty.service.dto.AppointmentDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AppointmentDtoValidator implements Validator {
	
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE);

	private final AppointmentService appointmentService;

	@Override
	public boolean supports(Class<?> clazz) {
		return AppointmentDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AppointmentDto appointmentDto = (AppointmentDto) target;
		Pattern datePattern = Pattern.compile(RegexConstant.DATE);
		Pattern timePattern = Pattern.compile(RegexConstant.TIME);
		String message;

		if(!datePattern.matcher(appointmentDto.getDate()).matches()) {
			message = String.format("Invalid date format [format=%s]", DateConstant.JAVA_DATE);
			errors.rejectValue("date", message, message);
		}else {
			if(!timePattern.matcher(appointmentDto.getTime()).matches()) {
				message = String.format("Invalid time format [format=%s]", DateConstant.JAVA_TIME);
				errors.rejectValue("time", message, message);
			}else {
				try {
					LocalDate date = LocalDate.parse(appointmentDto.getDate(), dateFormatter);
		            
					if(!date.isAfter(LocalDate.now())) {
						message = "Invalid date, must equal or after today";
						errors.rejectValue("date", message, message);
					}else if(appointmentService.isExist(appointmentDto.getMedicalProvider().getId(), date)) {
						message = "Data is already exist";
						errors.rejectValue("message", message, message);
					}
					
		        } catch (Exception e) {
		        	log.error(e.getLocalizedMessage(), e);
		        	errors.rejectValue("message", e.getLocalizedMessage(), e.getLocalizedMessage());
		        }
			}
		}
	}

}
