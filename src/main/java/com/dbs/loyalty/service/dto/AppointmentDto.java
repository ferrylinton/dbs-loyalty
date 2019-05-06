package com.dbs.loyalty.service.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Appointment Dto
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@ApiModel(value="Appointment", description = "Appointment's data")
public class AppointmentDto {
	
	private String message;
	
	private LocalDate arrivalDate;
	
	private HealthPartnerDto healthPartner;

}
