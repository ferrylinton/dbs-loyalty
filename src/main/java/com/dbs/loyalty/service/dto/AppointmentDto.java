package com.dbs.loyalty.service.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Appointment DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Appointment", description = "Appointment's data")
@Setter
@Getter
public class AppointmentDto {
	
	@ApiModelProperty(value = "Message", example = "Test", position = 0)
	private String message;
	
	@ApiModelProperty(value = "Arrival Date", example = "2019-09-20", position = 1)
	private LocalDate arrivalDate;
	
	@ApiModelProperty(position = 2)
	private MedicalProviderDto healthPartner;

}
