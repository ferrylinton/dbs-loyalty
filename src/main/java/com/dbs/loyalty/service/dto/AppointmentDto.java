package com.dbs.loyalty.service.dto;

import java.time.Instant;

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
	
	@ApiModelProperty(value = "Date", example = "2019-07-01T10:00:00.000Z", position = 1)
	private Instant date;
	
	@ApiModelProperty(position = 2)
	private MedicalProviderDto medicalProvider;
	
	@ApiModelProperty(position = 3)
	private MedicalProviderCityDto medicalProviderCity;

	@ApiModelProperty(position = 4)
	private MedicalProviderBranchDto medicalProviderBranch;
	
}
