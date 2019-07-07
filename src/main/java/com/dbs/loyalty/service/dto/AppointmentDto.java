package com.dbs.loyalty.service.dto;

import javax.annotation.Nonnull;

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
	
	@Nonnull
	@ApiModelProperty(value = "Date", example = "01-07-2019 10:00", position = 1)
	private String date;
	
	@Nonnull
	@ApiModelProperty(position = 2)
	private MedicalProviderDto medicalProvider;
	
	@ApiModelProperty(position = 3)
	private MedicalProviderCityDto medicalProviderCity;

	@ApiModelProperty(position = 4)
	private MedicalProviderBranchDto medicalProviderBranch;
	
	@Nonnull
	@ApiModelProperty(position = 5)
	private HealthPackageDto healthPackage;
	
	private String message;
	
}
