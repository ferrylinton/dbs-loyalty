package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="MedicalProviderCity", description = "MedicalProviderCity's data")
@Setter
@Getter
public class MedicalProviderCityDto {

	@ApiModelProperty(value = "MedicalProviderCity's id", example = "1IUe73HCc9Qfjl1sxhasNI", position = 0)
	private String id;
	
	@ApiModelProperty(value = "MedicalProviderCity's name", example = "Jakarta", position = 1)
	private String name;

}
