package com.dbs.loyalty.web.validator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dbs.loyalty.config.constant.RegexConstant;
import com.dbs.loyalty.service.AppointmentService;
import com.dbs.loyalty.service.DateService;
import com.dbs.loyalty.service.dto.AppointmentDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppointmentDtoValidator implements Validator {
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateService.JAVA_DATETIME);
	
	private final AppointmentService appointmentService;

	@Override
	public boolean supports(Class<?> clazz) {
		return AppointmentDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AppointmentDto appointmentDto = (AppointmentDto) target;
		Pattern pattern = Pattern.compile(RegexConstant.DATETIME);
		String message;

		if(!pattern.matcher(appointmentDto.getDate()).matches()) {
			message = String.format("Invalid date format [format=%s]", DateService.JAVA_DATETIME);
			errors.rejectValue("message", message, message);
		}else {
			try {
				LocalDateTime date = LocalDateTime.parse(appointmentDto.getDate(), formatter);
	            
				if(!date.isAfter(LocalDateTime.now().with(LocalTime.MIN))) {
					message = "Invalid date, must equal or after today";
					errors.rejectValue("message", message, message);
				}else if(appointmentService.isExist(appointmentDto.getMedicalProvider().getId(), date)) {
					message = "Data is already exist";
					errors.rejectValue("message", message, message);
				}
				
	        } catch (Exception e) {
	        	errors.rejectValue("message", e.getLocalizedMessage(), e.getLocalizedMessage());
	        }
		}
	}

}
